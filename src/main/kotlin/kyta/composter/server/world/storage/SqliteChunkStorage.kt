package kyta.composter.server.world.storage

import com.github.luben.zstd.ZstdInputStream
import com.github.luben.zstd.ZstdOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Path
import kotlinx.datetime.Clock
import kyta.composter.world.ChunkPos
import kyta.composter.world.block.AIR
import kyta.composter.world.block.Block
import kyta.composter.world.block.BlockState
import kyta.composter.world.chunk.Chunk
import kyta.composter.world.chunk.LightData.Companion.DEFAULT_LIGHT_VALUE
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.replace
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

private object Chunks : Table() {
    val key = long("key")
    val blocks = blob("blocks")
    val metadata = blob("metadata")
    val blockLight = blob("block_light")
    val skyLight = blob("sky_light")
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    override val primaryKey = PrimaryKey(key, name = "big_stonks")
}

class SqliteChunkStorage(directory: Path) : ChunkStorage {
    init {
        Database.connect("jdbc:sqlite:${File(directory.toFile(), "chunks.db")}", "org.sqlite.JDBC")
        transaction {
            SchemaUtils.create(Chunks)
        }
    }

    override suspend fun read(pos: ChunkPos): Chunk? = newSuspendedTransaction {
        val row = Chunks.selectAll().where { Chunks.key eq pos.compact }.firstOrNull()
            ?: return@newSuspendedTransaction null

        val chunk = Chunk(pos)
        val blocks = row[Chunks.blocks].deserialize()
        val metadata = row[Chunks.metadata].deserialize()
        val blockLight = row[Chunks.blockLight].deserialize()
        val skyLight = row[Chunks.skyLight].deserialize()

        for ((index, id) in blocks.withIndex()) {
            chunk.light.block[index] = blockLight[index]
            chunk.light.sky[index] = skyLight[index]

            if (id == AIR.networkId) continue
            chunk.states[index] = BlockState(Block(id), metadata[index])
        }

        chunk
    }

    override suspend fun write(pos: ChunkPos, chunk: Chunk) = newSuspendedTransaction {
        Chunks.replace {
            it[key] = pos.compact
            it[blocks] = chunk.states.map { state -> state?.block?.networkId ?: AIR.networkId }.serialize()
            it[metadata] = chunk.states.map { state -> state?.metadataValue ?: 0 }.serialize()
            it[blockLight] = chunk.light.block.map { light -> light ?: DEFAULT_LIGHT_VALUE }.serialize()
            it[skyLight] = chunk.light.sky.map { light -> light ?: DEFAULT_LIGHT_VALUE }.serialize()
        }

        return@newSuspendedTransaction
    }
}

private fun List<Int>.serialize(): ExposedBlob {
    val stream = ByteArrayOutputStream()

    ZstdOutputStream(stream, 9).use {
        it.setChecksum(true)
        it.write(map(Int::toByte).toByteArray())
    }

    return ExposedBlob(stream.toByteArray())
}

private fun ExposedBlob.deserialize(): List<Int> {
    val stream = ZstdInputStream(ByteArrayInputStream(bytes))
    return stream.readBytes().map(Byte::toInt).toList()
}