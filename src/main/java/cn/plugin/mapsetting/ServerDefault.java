package cn.plugin.mapsetting;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "server_default")
public class ServerDefault {
    @Id
    int id;

    @Column
    int map;
    int mapPlayer;
    int mapMobs;
    int mapItem;
    int mapOther;
    int mapTeam;
    int zoom;
    int size;
    int showCoords;
    int entity;

    String type;

}
