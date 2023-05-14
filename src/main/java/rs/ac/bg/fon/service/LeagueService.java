package rs.ac.bg.fon.service;

import rs.ac.bg.fon.entity.League;

import java.util.List;


public interface LeagueService {

    League save(League league);
    List<League> saveLeagues(List<League> leagues);
     List<League> getAllLeagues();
     boolean exists();
}
