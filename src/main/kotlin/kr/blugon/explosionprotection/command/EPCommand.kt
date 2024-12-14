package kr.blugon.explosionprotection.command

import kr.blugon.explosionprotection.ExplosionProtection.Companion.blockProtection
import kr.blugon.explosionprotection.ExplosionProtection.Companion.damageProtection
import kr.blugon.explosionprotection.ExplosionProtection.Companion.prefix
import kr.blugon.kotlinbrigadier.registerCommandHandler
import kr.blugon.minicolor.MiniColor.Companion.miniMessage
import org.bukkit.plugin.java.JavaPlugin

class EPCommand(plugin: JavaPlugin) {

    init {
        val manager = plugin.lifecycleManager
        manager.registerCommandHandler {
            register("explosionprotection", "Explosion Protection Command", "ep") {
                require { sender.hasPermission("explosionprotection.command") }

                then("block") {
                    then("on") {
                        executes {
                            sender.sendMessage(prefix.append("폭발에 의한 블럭파괴 보호를 활성화했습니다".miniMessage))
                            blockProtection = true
                        }
                    }
                    then("off") {
                        executes {
                            sender.sendMessage(prefix.append("폭발에 의한 블럭파괴 보호를 비활성화했습니다".miniMessage))
                            blockProtection = false
                        }
                    }
                }

                then("entity") {
                    then("on") {
                        executes {
                            sender.sendMessage(prefix.append("폭발 피해 보호를 활성화했습니다".miniMessage))
                            damageProtection = true
                        }
                    }
                    then("off") {
                        executes {
                            sender.sendMessage(prefix.append("폭발 피해 보호를 비활성화했습니다".miniMessage))
                            damageProtection = false
                        }
                    }
                }
            }
        }
    }
}