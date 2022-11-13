package com.game.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String title;

    @Column
    @Enumerated(EnumType.STRING)
    private Race race;

    @Column
    @Enumerated(EnumType.STRING)
    private Profession profession;

    @Column
    private Integer experience;

    @Column
    private Integer level;

    @Column
    private Integer untilNextLevel;

    @Column
    private Date birthday;

    @Column
    private Boolean banned;

    public Player(Long id, String name, String title, Race race, Profession profession, Integer experience,
                  Integer level, Integer untilNextLevel, Date birthday, Boolean banned) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = getCurrentPlayerXP(experience);
        this.level = level;
        this.untilNextLevel = getPlayerXPUntilNextLevel(experience, level);
        this.birthday = birthday;
        this.banned = banned;
    }

    public Player(Long id, String name, String title, Race race, Profession profession, Integer experience,
                  Integer level, Integer untilNextLevel, Date birthday) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = getCurrentPlayerXP(experience);
        this.level = level;
        this.untilNextLevel = getPlayerXPUntilNextLevel(experience, level);
        this.birthday = birthday;

        this.banned = false;        // alternative variant of constructor (without "banned" field)
    }

    public Player() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public Race getRace() {
        return race;
    }

    public Profession getProfession() {
        return profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", race=" + race +
                ", profession=" + profession +
                ", experience=" + experience +
                ", level=" + level +
                ", untilNextLevel=" + untilNextLevel +
                ", birthday=" + birthday +
                ", banned=" + banned +
                '}';
    }

    private Integer getCurrentPlayerXP(Integer experience){
        int result = (int) (Math.sqrt(2500 + 200 * experience) - 50) / 100;
        return result;
    }

    private Integer getPlayerXPUntilNextLevel(Integer level, Integer experience){
        int result = 50 * (level + 1) * (level + 2) - experience;
        return result;
    }
}