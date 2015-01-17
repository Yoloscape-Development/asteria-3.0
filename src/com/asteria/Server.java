package com.asteria;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.asteria.game.character.player.content.RestoreStatTask;
import com.asteria.game.character.player.minigame.MinigameHandler;
import com.asteria.game.item.ItemNodeManager;
import com.asteria.task.TaskManager;
import com.asteria.utility.Settings;
import com.asteria.utility.Utility;

/**
 * The main class of <i>Asteria 3.0</i> that will prepare the server builder.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class Server {

    /**
     * The logger that will print important information.
     */
    private static Logger logger = Utility.getLogger(Server.class);

    /**
     * The server builder that will prepare the server.
     */
    private static ServerBuilder builder;

    /**
     * The default constructor.
     * 
     * @throws InstantiationException
     *             if this class is instantiated.
     */
    private Server() {
        throw new InstantiationError("This class cannot be instantiated!");
    }

    /**
     * The main method of <i>Asteria 3.0</i>.
     * 
     * @param args
     *            all of the runtime arguments.
     */
    public static void main(String[] args) {
        try {
            boolean concurrent = (Runtime.getRuntime().availableProcessors() > 1);
            builder = new ServerBuilder().setParallelEngine(concurrent).setServerPort(Settings.PORT);
            builder.build();

            logger.info(Settings.NAME + " is now online!");
            TaskManager.submit(new ItemNodeManager());
            TaskManager.submit(new RestoreStatTask());
            TaskManager.submit(new MinigameHandler());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occured while starting " + Settings.NAME + "!", e);
            System.exit(1);
        }
    }

    /**
     * Gets the server builder that will prepare the server.
     * 
     * @return the server builder.
     */
    public static ServerBuilder getBuilder() {
        return builder;
    }
}