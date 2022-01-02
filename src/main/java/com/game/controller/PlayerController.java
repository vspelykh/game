package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {

    private PlayerService service;

    public PlayerController(PlayerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Race race,
            @RequestParam(required = false) Profession profession,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean banned,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel,
            @RequestParam(required = false) PlayerOrder order,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "3") Integer pageSize) {

        if (order == null){
            order = PlayerOrder.ID;
        }
        List<Player> playerList = service.getAllPlayers();
        playerList = playerList.stream().sorted(new Player.PlayerComparator(order)).
                filter(player -> name == null || player.getName().contains(name)).
                filter(player -> title == null || player.getTitle().contains(title)).
                filter(player -> race == null || player.getRace().equals(race)).
                filter(player -> profession == null || player.getProfession().equals(profession)).
                filter(player -> after == null || player.getBirthday().getTime() >= after).
                filter(player -> before == null || player.getBirthday().getTime() <= before).
                filter(player -> banned == null || player.getBanned() == banned).
                filter(player -> minExperience == null || player.getExperience() >= minExperience).
                filter(player -> maxExperience == null || player.getExperience() <= maxExperience).
                filter(player -> minLevel == null || player.getLevel() >= minLevel).
                filter(player -> maxLevel == null || player.getLevel() <= maxLevel).skip((long) pageNumber * pageSize).
                limit(pageSize).
                collect(Collectors.toList());
        return new ResponseEntity<>(playerList, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> countPlayers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Race race,
            @RequestParam(required = false) Profession profession,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean banned,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel
    ){
        Integer count = (int) service.getAllPlayers().stream().
        filter(player -> name == null || player.getName().contains(name)).
                filter(player -> title == null || player.getTitle().contains(title)).
                filter(player -> race == null || player.getRace().equals(race)).
                filter(player -> profession == null || player.getProfession().equals(profession)).
                filter(player -> after == null || player.getBirthday().getTime() >= after).
                filter(player -> before == null || player.getBirthday().getTime() <= before).
                filter(player -> banned == null || player.getBanned() == banned).
                filter(player -> minExperience == null || player.getExperience() >= minExperience).
                filter(player -> maxExperience == null || player.getExperience() <= maxExperience).
                filter(player -> minLevel == null || player.getLevel() >= minLevel).
                filter(player -> maxLevel == null || player.getLevel() <= maxLevel).count();

        return new ResponseEntity<>(count, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player){
        if (player.isValid()){
            player.countAndSetLevelParams();
            if (!player.getBanned()){
                player.setBanned(false);
            }
            service.savePlayer(player);
            return new ResponseEntity<>(player, HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long id){

        if (id <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!service.isExist(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Player player = service.getPlayer(id);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@RequestBody Player player, @PathVariable Long id){

        if (id <= 0 || !player.isValidToUpdate())
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!service.isExist(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        Player currentPlayer = service.getPlayer(id);
        if (player.getName() != null )
            currentPlayer.setName(player.getName());
        if (player.getTitle() != null)
            currentPlayer.setTitle(player.getTitle());
        if (player.getRace() != null)
            currentPlayer.setRace(player.getRace());
        if (player.getProfession() != null)
            currentPlayer.setProfession(player.getProfession());
        if (player.getBirthday() != null)
            currentPlayer.setBirthday(player.getBirthday());
        if (player.getBanned() != null)
            currentPlayer.setBanned(player.getBanned());
        if (player.getExperience() != null) {
            currentPlayer.setExperience(player.getExperience());
            currentPlayer.countAndSetLevelParams();
        }

        service.savePlayer(currentPlayer);
        return new ResponseEntity<>(currentPlayer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable Long id){

        if (id <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!service.isExist(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Player player = service.getPlayer(id);
        service.deletePlayer(player);

        return new ResponseEntity<>(player, HttpStatus.OK);
    }
}
