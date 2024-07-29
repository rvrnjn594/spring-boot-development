package io.datajek.tennis_player_rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository repo;

    // Get all players
    public List<Player> getAllPlayers() {
        return repo.findAll();
    }
    // Get player by ID
    public Player getPlayer(int id) {
        Optional<Player> tempPlayer = repo.findById(id);
        Player p = null;

        // if the Optioanl has a value, assign it to p
        if (tempPlayer.isPresent()) p = tempPlayer.get();
        return p;
    }
    // Add a player
    public Player addPlayer(Player p) {
        return repo.save(p);
    }
    // Update a player
    public Player updatePlayer(int id, Player p) {
        Optional<Player> tempPlayer = repo.findById(id);

        if (tempPlayer.isEmpty())
            throw new RuntimeException("Player with id {" + id + "} not found");

        p.setId(id);
        return repo.save(p);
    }
    // Partial update

    // delete a player

}
