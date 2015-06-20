package pt.ipleiria.estg.GEDD;

import java.util.LinkedList;

import pt.ipleiria.estg.GEDD.Models.Game;
import pt.ipleiria.estg.GEDD.Models.Goalkeeper;
import pt.ipleiria.estg.GEDD.Models.Player;

/**
 * Created by Andre on 17/06/2015.
 */
public class GmailApiTaskParams {
    LinkedList<Player> players;
    Game game;
    LinkedList<Goalkeeper> gks;

    GmailApiTaskParams(LinkedList<Player> players, Game game, LinkedList<Goalkeeper> gks) {
        this.players = players;
        this.game = game;
        this.gks = gks;
    }
}
