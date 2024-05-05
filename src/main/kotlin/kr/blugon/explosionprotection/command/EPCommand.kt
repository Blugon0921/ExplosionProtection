package kr.blugon.explosionprotection.command

import kr.blugon.explosionprotection.ExplosionProtection.Companion.blockProtection
import kr.blugon.explosionprotection.ExplosionProtection.Companion.damageProtection
import kr.blugon.explosionprotection.ExplosionProtection.Companion.prefix
import kr.blugon.minicolor.MiniColor
import kr.blugon.minicolor.MiniColor.Companion.miniMessage
import net.kyori.adventure.text.Component.text
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class EPCommand : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(command.name != "explosionprotection" && command.name != "ep") return false
        if(!sender.isOp && !sender.hasPermission("explosionprotection.command")) {
            sender.sendMessage(prefix.append("${MiniColor.RED}권한이 부족합니다".miniMessage))
            return false
        }
        if(args.size != 2) return sender.sendHowToUse()

        val targetName = when(args[0]) {
            "block" -> "폭발에 의한 블럭파괴 보호"
            "entity" -> "폭발 피해 보호"
            else -> return sender.sendHowToUse()
        }
        val enableMessage = when(args[1]) {
            "on" -> "활성화"
            "off" -> "비활성화"
            else -> return sender.sendHowToUse()
        }
        sender.sendMessage(prefix.append("${targetName}를 ${enableMessage}했습니다".miniMessage))
        when(args[0]) {
            "block" -> when(args[1]) {
                "on" -> blockProtection = true
                "off" -> blockProtection = false
            }
            "entity" -> when(args[1]) {
                "on" -> damageProtection = true
                "off" -> damageProtection = false
            }
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
        if(command.name != "explosionprotection" && command.name != "ep") return null
        if(!sender.isOp && !sender.hasPermission("explosionprotection.command")) return mutableListOf()
        val returns = when(args.size) {
            1 -> mutableListOf("block", "damage")
            2 -> mutableListOf("on", "off")
            else -> return mutableListOf()
        }

        val final = mutableListOf<String>()
        for(r in returns) if(r.startsWith(args[args.size - 1])) final.add(r)
        return final
    }

    private fun CommandSender.sendHowToUse(response: Boolean = false): Boolean {
        this.sendMessage(prefix.append("명령어 사용법: /ep <block|damage> <on|off>".miniMessage))
        return response
    }
}