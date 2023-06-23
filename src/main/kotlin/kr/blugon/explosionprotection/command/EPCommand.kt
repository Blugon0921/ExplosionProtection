package kr.blugon.explosionprotection.command

import kr.blugon.explosionprotection.ExplosionProtection.Companion.blockExplosion
import kr.blugon.explosionprotection.ExplosionProtection.Companion.explosionDamage
import net.kyori.adventure.text.Component.text
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.*

class EPCommand : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(command.name != "ep") return false
        if(!sender.isOp) {
            sender.sendMessage(text("§7[§9ExplosionProtection§7] §f당신은 이 명령어를 사용할 권한이 없습니다"))
            return true
        }
        if(args.isEmpty()) {
            sender.sendMessage(text("§7[§9ExplosionProtection§7] §f명령어 사용법: /ep <block|damage> <on|off>"))
            return true
        }
        if(args[0] == "block") {
            if(args[1] == "on") {
                sender.sendMessage(text("§7[§9ExplosionProtection§7] §f폭발에 의한 블럭파괴를 비활성화 했습니다"))
                blockExplosion = false
            } else if(args[1] == "off") {
                sender.sendMessage(text("§7[§9ExplosionProtection§7] §f폭발에 의한 블럭파괴를 활성화 했습니다"))
                blockExplosion = true
            } else sender.sendMessage(text("§7[§9ExplosionProtection§7] §f명령어 사용법: /ep <block|damage> <on|off>"))
        } else if(args[0] == "damage") {
            if(args[1] == "on") {
                sender.sendMessage(text("§7[§9ExplosionProtection§7] §f폭발에 의해 엔티티가 받는 피해를 비활성화 했습니다"))
                explosionDamage = false
            } else if(args[1] == "off") {
                sender.sendMessage(text("§7[§9ExplosionProtection§7] §f폭발에 의해 엔티티가 받는 피해를 활성화 했습니다"))
                explosionDamage = true
            } else sender.sendMessage(text("§7[§9ExplosionProtection§7] §f명령어 사용법: /ep <block|damage> <on|off>"))
        } else sender.sendMessage(text("§7[§9ExplosionProtection§7] §f명령어 사용법: /ep <block|damage> <on|off>"))
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String>? {
        if(command.name != "ep") return Collections.emptyList()
        if(!sender.isOp) return Collections.emptyList()
        if(args == null) return Collections.emptyList()
        var returns = mutableListOf<String>()
        if(args.size == 1) returns = mutableListOf("block", "damage")
        else if(args.size == 2) returns =  mutableListOf("on", "off")

        val final = arrayListOf<String>()
        for(r in returns) {
            if(r.startsWith(args[args.size - 1])) final.add(r)
        }
        return final
    }
}