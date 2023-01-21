package me.alientation.doomboheadplugin.customcommand;

import me.alientation.doomboheadplugin.customcommand.annotations.commands.CommandAnnotation;
import me.alientation.doomboheadplugin.customcommand.annotations.commands.CommandTabAnnotation;
import me.alientation.doomboheadplugin.customcommand.annotations.permissions.PermissionAnnotation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TestCustomCommand extends CustomCommandAPI {
    @CommandAnnotation(id = "alphabet", name = "alphabet",
            description = "Alphabet Command",
            usage = "/alphabet",
            aliases = {"abc"})
    @PermissionAnnotation(permission = "alphabet", required = false)
    public boolean helpCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 0) return false;
        sender.sendMessage("No alphabet for you loser");
        return true;
    }


    @CommandAnnotation(id = "alphabet.list", name = "list")
    @PermissionAnnotation(permission = "alphabet.list", required = false)
    public boolean helpListCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("Alphabet\n-----------------------\nabcdefghijklmnopqrstuvwxyz\n------------------------");
        return true;
    }


    @CommandAnnotation(id = "alphabet.list.add", name = "add")
    @PermissionAnnotation(permission = "alphabet.add", required = true)
    @PermissionAnnotation(permission = "admin", required = false)
    public boolean helpAddCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("no.");
        return true;
    }


    @CommandAnnotation(id = "alphabet.hello", name = "hello", aliases = {"hi"})
    @PermissionAnnotation(permission = "alphabet",required=false)
    public boolean helpHelloCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("hi");
        return true;
    }


    @CommandTabAnnotation(id = "alphabet.hello", name = "hello")
    public List<String> helpHelloTab(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<>();
        list.add("SIRI");
        return list;
    }
}
