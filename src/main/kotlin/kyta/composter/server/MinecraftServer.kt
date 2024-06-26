package kyta.composter.server

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.asCoroutineDispatcher
import kyta.composter.network.NetworkController
import net.kyori.adventure.text.flattener.ComponentFlattener
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.slf4j.LoggerFactory
import xyz.nkomarn.composter.Composter
import java.nio.file.Paths
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.Executor
import kyta.composter.server.world.WorldManager

class MinecraftServer(val composter: Composter) : Tickable, CoroutineScope {
    override val coroutineContext = Executor { executeServerTask(it) }.asCoroutineDispatcher()
    private val mainThreadTasks = ConcurrentLinkedDeque<Runnable>()
    private val networkController = NetworkController(this)
    val logger = LoggerFactory.getLogger("server")
    val worldManager = WorldManager(this, Paths.get("worlds"))
    val playerList = PlayerList(this)

    fun startServer() {
        worldManager.load()

        try {
            networkController.bind(25565)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }

    override fun tick(currentTick: Long) {
        worldManager.tick(currentTick)
        playerList.tick(currentTick)

        /* process tasks that are waiting */
        while (!mainThreadTasks.isEmpty()) {
            try {
                mainThreadTasks.pop().run()
            } catch (x: Throwable) {
                logger.error("encountered an error while processing a main thread task", x)
            }
        }
    }

    private fun executeServerTask(task: Runnable) {
        mainThreadTasks.add(task)
    }

    companion object {
        private val SERIALIZER = PlainTextComponentSerializer.builder()
            .flattener(ComponentFlattener.basic())
            .build()
    }
}
