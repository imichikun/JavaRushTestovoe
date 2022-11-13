package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class MyRestController {
    @Autowired
    private PlayerService playerService;

    @GetMapping("/players")
    public List<Player> getAllPlayers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(defaultValue = "ID", value = "order") PlayerOrder order,
            @RequestParam(defaultValue = "0", value = "pageNumber") Integer pageNumber,
            @RequestParam(defaultValue = "3", value = "pageSize") Integer pageSize
    ){
        List<Player> playersList = playerService.getAllPlayers(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel);
        return playerService.sortingPlayers(playersList, pageNumber, pageSize, order);
    }

    @GetMapping("/players/{id}")
    public Player getPlayersById(@PathVariable(value = "id") String id){
        return playerService.getPlayerById(id);
    }

    @PostMapping("/players")
    public Player addPlayer(@RequestBody Player player){
        return playerService.createPlayer(player);
    }

    @PutMapping("/players/{id}")
    public Player updatePlayer(@PathVariable (value = "id") String id, @RequestBody Player player){
        if (player.getName() == null && player.getTitle() == null && player.getProfession() == null
                && player.getRace() == null && player.getBirthday() == null && player.getExperience() == null)
            return playerService.getPlayerById(id);

        return playerService.updatePlayer(player, playerService.getPlayerById(id));
    }

    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable(value = "id") String id){
        playerService.deletePlayer(id);
    }

    @GetMapping("/players/count")
    public Integer getAll(@RequestParam(value="name", required = false) String name,
                          @RequestParam(value = "title", required = false) String title,
                          @RequestParam(value = "race", required = false) Race race,
                          @RequestParam(value = "profession", required = false) Profession profession,
                          @RequestParam(value = "after", required = false) Long after,
                          @RequestParam(value = "before", required = false) Long before,
                          @RequestParam(value = "banned", required = false) Boolean banned,
                          @RequestParam(value = "minExperience", required = false) Integer minExperience,
                          @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                          @RequestParam(value = "minLevel", required = false) Integer minLevel,
                          @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {
        List<Player> playersList = playerService.getAllPlayers(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
        return playersList.size();
    }
}