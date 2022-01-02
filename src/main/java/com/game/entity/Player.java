package com.game.entity;

import com.game.controller.PlayerOrder;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "title")
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(name = "race")
    private Race race;
    @Enumerated(EnumType.STRING)
    @Column(name = "profession")
    private Profession profession;
    @Column(name = "experience")
    private Integer experience;
    @Column(name = "level")
    private Integer level ;
    @Column(name = "untilNextLevel")
    private Integer untilNextLevel;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "banned")
    private Boolean banned;

    public Player(Long id, String name, String title, Race race, Profession profession, Integer experience, Integer level, Integer untilNextLevel, Date birthday, Boolean banned) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.level = level;
        this.untilNextLevel = untilNextLevel;
        this.birthday = birthday;
        this.banned = banned;
    }


    public Player() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
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


    public boolean isValid(){
        if (name == null || name.equals("")|| name.length() > 12){
            return false;
        }
        if (title == null || title.equals("") || title.length() > 30){
            return false;
        }
        if (race == null || profession == null){
            return false;
        }
        if (experience < 0 || experience > 10_000_000){
            return false;
        }
        else return true;
    }
    public boolean isValidToUpdate(){

        Date current = new Date();
        if (name != null && (name.length() > 12 || name.equals(""))) {
            return false;
        }

        if (title != null && (title.length() > 30 || title.equals(""))) {
            return false;
        }

        if (experience != null && (experience < 0 || experience > 10000000)) {
            return false;
        }

        return birthday == null || (birthday.after(new Date(0)) && current.after(birthday));
    }

    public void countAndSetLevelParams() {
        setLevel((int) (((Math.sqrt(250 + (200 * experience))) - 50)/100));
        setUntilNextLevel(50 * (level+1) * (level+2) - experience);
    }

    public static class PlayerComparator implements Comparator<Player> {

        private final PlayerOrder order;

        public PlayerComparator(PlayerOrder order) {
            this.order = order;
        }

        @Override
        public int compare(Player o1, Player o2) {
            switch (order){
                case ID:
                    return (int) (o1.getId() - o2.getId());
                case NAME:
                    return o1.getName().compareTo(o2.getName());

                case EXPERIENCE:
                    return o1.getExperience() - o2.getExperience();
                case BIRTHDAY:
                    return o1.getBirthday().compareTo(o2.getBirthday());
                case LEVEL:
                    return o1.getLevel() - o2.getLevel();
                default:
                    return 0;
            }
        }
    }
}
