package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PlayerService {

    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    public List<Player> getAllPlayers() {
        return repository.findAll();
    }

    public boolean isExist(Long id) {
        return repository.existsById(id);
    }

    public Player getPlayer(Long id) {
        return repository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public void deletePlayer(Player player) {
        repository.delete(player);
    }

    public void savePlayer(Player player) {
        repository.save(player);
    }
}
