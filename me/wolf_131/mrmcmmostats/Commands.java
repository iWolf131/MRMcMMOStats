package me.wolf_131.mrmcmmostats;

import com.gmail.nossr50.mcMMO;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("Apenas jogadores teem acesso a este comando.");
            return false;
        }
        Player p = (Player) sender;
        if (!mcMMO.getDatabaseManager().getStoredUsers().contains(p.getName())) {
            p.sendMessage("§cVocê não possui nenhum histórico de mcMMO.");
            return false;
        }
        Main.openMenu(p);
        return true;
    }
}
