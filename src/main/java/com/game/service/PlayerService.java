package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> getAllPlayers(String name, String title, Race race, Profession profession, Long after,
        Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel) {

        List<Player> playersList = new ArrayList<>();
        Date afterDate = after == null ? null : new Date(after);
        Date beforeDate = before == null ? null : new Date(before);

        playerRepository.findAll().forEach(player -> {
            if (player.getName().contains(name))
                playersList.add(player);
            if (player.getTitle().contains(title))
                playersList.add(player);
            if(player.getRace().equals(race))
                playersList.add(player);
            if(player.getProfession().equals(profession))
                playersList.add(player);
            if(player.getBirthday().before(afterDate))
                playersList.add(player);
            if(player.getBirthday().after(beforeDate))
                playersList.add(player);
            if(player.getBanned().equals(banned))
                playersList.add(player);
            if(player.getExperience().compareTo(minExperience) > 0)
                playersList.add(player);
            if(player.getExperience().compareTo(maxExperience) < 0)
                playersList.add(player);
            if(player.getLevel().compareTo(minLevel) > 0)
                playersList.add(player);
            if(player.getLevel().compareTo(maxLevel) < 0)
                playersList.add(player);

        });
        return playersList;
    }

    public List<Player> sortingPlayers(List<Player> playersList, Integer pageNumber, Integer countOnPage, PlayerOrder order){
        int pageNum = pageNumber + 1;
        int count = countOnPage;
        List<Player> resultList = new ArrayList<>();

        if (order.equals(PlayerOrder.NAME))
            playersList.sort(Comparator.comparing(Player::getName));

        else if (order.equals(PlayerOrder.EXPERIENCE))
            playersList.sort(Comparator.comparing(Player::getExperience));

        else if (order.equals(PlayerOrder.BIRTHDAY))
            playersList.sort(Comparator.comparing(Player::getBirthday));

        for (int i = pageNum * count - (count - 1) - 1; i < count * pageNum && i < playersList.size(); i++) {
            resultList.add(playersList.get(i));
        }

        return resultList;
    }

    public Player createPlayer(Player player) {
        if ( player.getName() == null
             || player.getTitle() == null
             || player.getRace() == null
             || player.getProfession() == null
             || player.getBirthday() == null
             || player.getExperience() == null

             || player.getTitle().length() > 30
             || player.getName().length() > 12
             || player.getName().equals("")
             || player.getExperience() < 0
             || player.getExperience() > 10000000
             || player.getBirthday().getTime() < 0
             || player.getBirthday().before(new Date(946684800000L))
             || player.getBirthday().after(new Date(32503680000000L))
         )
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "requirements for creating a new player are not met");

        player.setLevel((int) (Math.sqrt((double) 2500 + 200 * player.getExperience()) - 50) / 100);
        player.setUntilNextLevel(50 * (player.getLevel() + 1) * (player.getLevel() + 2) - player.getExperience());

        return playerRepository.save(player);
    }

    public Player updatePlayer(Player playerNew, Player playerOld) {
        if (playerNew.getName() != null)
            playerOld.setName(playerNew.getName());
        if (playerNew.getTitle() != null)
            playerOld.setTitle(playerNew.getTitle());
        if (playerNew.getProfession() != null)
            playerOld.setProfession(playerNew.getProfession());
        if (playerNew.getRace() != null)
            playerOld.setRace(playerNew.getRace());
        if (playerNew.getExperience() != null) {
                if (validExperience(playerNew.getExperience()))
                    playerOld.setExperience(playerNew.getExperience());
                else
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (playerNew.getBirthday() != null) {
            if (validDate(playerNew.getBirthday()))
                playerOld.setBirthday(playerNew.getBirthday());
            else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (playerNew.getBanned() != null)
            playerOld.setBanned(playerNew.getBanned());

        playerOld.setLevel((int) ((Math.sqrt(2500 + 200 * playerOld.getExperience()) - 50) / 100));
        playerOld.setUntilNextLevel(50 * (playerOld.getLevel() + 1) * (playerOld.getLevel() + 2) - playerOld.getExperience());

        if (playerNew.getBirthday() != null)
            playerOld.setBirthday(playerNew.getBirthday());

        return playerRepository.save(playerOld);
    }

    public Player getPlayerById(String id) {
        Long checkedId = validId(id);

        if ( !playerRepository.existsById(checkedId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return playerRepository.findById(checkedId).get();
    }

    public void deletePlayer(String id) {
        Long checkedId = validId(id);

        if(!playerRepository.existsById(checkedId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        playerRepository.deleteById(checkedId);
    }

    private Long validId(String id){
        Long checkedId;
        try{
            checkedId = Long.parseLong(id);
        } catch(NumberFormatException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if( checkedId <= 0 )
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        return checkedId;
    }

    private boolean validDate(Date date) {
        if (date == null)
            return false;

        Calendar calendar = Calendar.getInstance();
        calendar.set(1999, 11, 31);
        Date after = calendar.getTime();
        calendar.set(3000, 11, 31);
        Date before = calendar.getTime();

        return (date.before(before) && date.after(after));
    }

    private boolean validExperience(Integer experience) {
        return experience >= 0 && experience <= 10000000;
    }
}