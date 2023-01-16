package me.alientation.doomboheadplugin.customcommand;

import me.alientation.doomboheadplugin.customcommand.annotations.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TestCustomCommand extends CustomCommandAPI {
    @CommandAnnotation(id = "help", name = "help",
            description = "Help Command",
            aliases = {"helpme"})
    @PermissionAnnotation(permission = "help", required = false)
    public boolean helpCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("No help for you loser");
        return true;
    }


    @CommandAnnotation(id = "help.list", name = "list")
    @PermissionAnnotation(permission = "help.list", required = false)
    public boolean helpListCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("Help List\n-----------------------\n1) nothing\n2) helps\n------------------------");
        return true;
    }


    @CommandAnnotation(id = "help.list.add", name = "add")
    @PermissionAnnotation(permission = "help.add", required = true)
    @PermissionAnnotation(permission = "admin", required = false)
    public boolean helpAddCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("oink");
        return true;
    }


    @CommandAnnotation(id = "help.hello", name = "hello", aliases = {"hi"})
    @PermissionAnnotation(permission = "help",required=false)
    public boolean helpHelloCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("hi");
        return true;
    }


    @CommandTabAnnotation(id = "help.hello", name = "hello")
    public List<String> helpHelloTab(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<>();
        list.add("SIRI");
        return list;
    }
}
