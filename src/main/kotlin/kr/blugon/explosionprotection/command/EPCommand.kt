package kr.blugon.explosionprotection.command

import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import kr.blugon.explosionprotection.ExplosionProtection.Companion.blockProtection
import kr.blugon.explosionprotection.ExplosionProtection.Companion.damageProtection
import kr.blugon.explosionprotection.ExplosionProtection.Companion.prefix
import kr.blugon.kotlinbrigadier.registerEventHandler
import kr.blugon.kotlinbrigadier.sender
import kr.blugon.minicolor.MiniColor
import kr.blugon.minicolor.MiniColor.Companion.miniMessage
import net.kyori.adventure.text.Component.text
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin

class EPCommand(plugin: JavaPlugin) {

    init {
        val manager = plugin.lifecycleManager
        manager.registerEventHandler {
            register("explosionprotection", "Explosion Protection Command", "ep") {
                require { sender.hasPermission("explosionprotection.command") }

                then("block") {
                    then("on") {
                        executes {
                            sender.sendMessage(prefix.append("폭발에 의한 블럭파괴 보호를 활성화했습니다".miniMessage))
                            blockProtection = true
                            true
                        }
                    }
                    then("off") {
                        executes {
                            sender.sendMessage(prefix.append("폭발에 의한 블럭파괴 보호를 비활성화했습니다".miniMessage))
                            blockProtection = false
                            true
                        }
                    }
                }

                then("entity") {
                    then("on") {
                        executes {
                            sender.sendMessage(prefix.append("폭발 피해 보호를 활성화했습니다".miniMessage))
                            damageProtection = true
                            true
                        }
                    }
                    then("off") {
                        executes {
                            sender.sendMessage(prefix.append("폭발 피해 보호를 비활성화했습니다".miniMessage))
                            damageProtection = false
                            true
                        }
                    }
                }
            }
        }
    }
}