package cn.plugin.mapsetting;

import com.avaje.ebean.EbeanServer;
import com.mengcraft.simpleorm.EbeanHandler;
import com.mengcraft.simpleorm.EbeanManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MapSetting extends JavaPlugin implements Listener {

    @Getter
    private static EbeanServer ebeanServer;

    @Getter
    private static ServerDefault serverInfo = null;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        EbeanManager manager = Bukkit.getServer().getServicesManager()
                .getRegistration(EbeanManager.class)
                .getProvider();
        EbeanHandler handler = manager.getHandler(this);

        if (!handler.isInitialized()) {
            handler.define(ServerDefault.class);
            try {
                handler.initialize();
            } catch (Exception e) {
                // Do what you want to do.
            }
        }
        // This function will inject into Bukkit's build-in
        // ORM support.
        handler.reflect();
        // This function will try to create not exists tables.
        handler.install();
        // Injected build-in method. Return initialized
        // Ebean server.
        ebeanServer = manager.getHandler(this).getServer();
        // EbeanServer server = handler.getServer();

        loadAllInfo();
    }

    private void loadAllInfo() {
        serverInfo = ebeanServer.find(ServerDefault.class).where().eq("type", getConfig().getString("ServerType")).findUnique();
        System.out.print(getCodeToSend(serverInfo));
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        if (serverInfo != null) {
            System.out.print(getNewCodeToSend(serverInfo));
//            event.getPlayer().sendMessage(getCodeString(serverInfo));
//            event.getPlayer().sendMessage(getNewCodeString(serverInfo));
        }
    }

    public static String getCodeString(ServerDefault serverInfo) {
        String toReturn = "§c §r§5 §r§1 §r§f";
        String code = String.valueOf(getCodeToSend(serverInfo));
        int size = code.length();
        for (int i = 0; i < size; i++) {
            toReturn = toReturn + " " + "§r§" + code.substring(i, i + 1);
        }
        toReturn = toReturn + " ";

        return toReturn;
    }

    public static String getNewCodeString(ServerDefault serverInfo) {
        String toReturn = "§c §r§5 §r§1 §r§梦";
        String code = String.valueOf(getNewCodeToSend(serverInfo));
        int size = code.length();
        for (int i = 0; i < size; i++) {
            toReturn = toReturn + " " + "§r§" + code.substring(i, i + 1);
        }
        toReturn = toReturn + " ";

        return toReturn;
    }

    private static String getNewCodeToSend(ServerDefault serverInfo) {
        String code = "";
        code = code + "zoom:" + serverInfo.getZoom() +";";
        code = code + "size:" + serverInfo.getSize() +";";
        code = code + "showcoords:" + serverInfo.getShowCoords() +";";
        code = code + "entity:" + serverInfo.getEntity() +";";
        return code;
    }

    public static int getCodeToSend(ServerDefault serverInfo) {
        int code = 0;
        code |= (serverInfo.getMap() == 1 ? 1 : 0);
        code |= (serverInfo.getMapPlayer() == 1 ? 1024 : 0);
        code |= (serverInfo.getMapMobs() == 1 ? 2048 : 0);
        code |= (serverInfo.getMapItem() == 1 ? 4096 : 0);
        code |= (serverInfo.getMapOther() == 1 ? 8192 : 0);
        code |= (serverInfo.getMapTeam() == 1 ? 32768 : 0);
        return code;
    }

}
