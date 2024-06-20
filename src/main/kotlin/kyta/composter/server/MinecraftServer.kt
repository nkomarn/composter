package kyta.composter.server

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.asCoroutineDispatcher
import kyta.composter.Tickable
import kyta.composter.network.NetworkController
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.flattener.ComponentFlattener
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.slf4j.LoggerFactory
import xyz.nkomarn.composter.Composter
import xyz.nkomarn.composter.command.CommandSource
import xyz.nkomarn.composter.server.WorldManager
import java.nio.file.Paths
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.Executor

class MinecraftServer(val composter: Composter) : Tickable, CommandSource, CoroutineScope {
    override val coroutineContext = Executor { executeServerTask(it) }.asCoroutineDispatcher()
    private val networkController = NetworkController(this)
    private val mainThreadTasks = ConcurrentLinkedDeque<Runnable>()
    val worldManager = WorldManager(this, Paths.get("worlds"))
    val playerList = PlayerList(this)
    val logger = LoggerFactory.getLogger("server")

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
//         logger.info("queued task from {} thread..", Thread.currentThread().getName());
        mainThreadTasks.add(task)
    }

    override fun getName(): String {
        return "Server"
    }

    override fun sendMessage(message: Component) {
        /*
         * TODO: Console color support.
         */
        logger.info(SERIALIZER.serialize(message))
    }

    companion object {
        private val SERIALIZER = PlainTextComponentSerializer.builder()
            .flattener(ComponentFlattener.basic())
            .build()
    }
}
