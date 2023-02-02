package kr.blugon.explosionprotection

import io.github.monun.kommand.kommand
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.data.type.Bed
import org.bukkit.block.data.type.RespawnAnchor
import org.bukkit.block.data.type.TNT
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class ExplosionProtection : JavaPlugin(),Listener {

    var config = File(dataFolder, "config.yml")
    val yaml = YamlConfiguration.loadConfiguration(config)

    var blockExplosion = false
    var explosionDamage = true

    override fun onEnable() {
        logger.info("Plugin Enable")
        Bukkit.getPluginManager().registerEvents(this, this)


        if(!config.exists()) {
            yaml.set("blockExplosion", false)
            yaml.set("explosionDamage", false)
            yaml.save(config)
        }
        yaml.load(config)
        onCommand()

        blockExplosion = yaml.getBoolean("blockExplosion")
        explosionDamage = yaml.getBoolean("explosionDamage")
    }

    override fun onDisable() {
        yaml.set("blockExplosion", blockExplosion)
        yaml.set("explosionDamage", explosionDamage)
        yaml.save(config)
        logger.info("Plugin Disable")
    }

    fun onCommand() {
        kommand {
            register("explosionprotection", "ep") {
                requires {
                    isOp
                }

                then("block") {
                    then("on") {
                        executes {
                            sender.sendMessage("§7[§9ExplosionProtection§7] §f폭발에 의한 블럭파괴를 비활성화 했습니다")
                            blockExplosion = false
                        }
                    }
                    then("off") {
                        executes {
                            sender.sendMessage("§7[§9ExplosionProtection§7] §f폭발에 의한 블럭파괴를 활성화 했습니다")
                            blockExplosion = true
                        }
                    }
                }
                then("damage") {
                    then("on") {
                        executes {
                            sender.sendMessage("§7[§9ExplosionProtection§7] §f폭발에 의해 엔티티가 받는 피해를 비활성화 했습니다")
                            explosionDamage = false
                        }
                    }
                    then("off") {
                        executes {
                            sender.sendMessage("§7[§9ExplosionProtection§7] §f폭발에 의해 엔티티가 받는 피해를 활성화 했습니다")
                            explosionDamage = true
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    fun blockExplode(event : BlockExplodeEvent) {
        val block = event.block
        val world = block.world
        val location = block.location.add(.5, .0, .5)
        if(!blockExplosion) {
            event.isCancelled = true
            world.createExplosion(location, event.yield, false, false)
        }
    }

    @EventHandler
    fun entityExplode(event : EntityExplodeEvent) {
        val entity = event.entity
        val world = entity.world
        val location = entity.location.add(.5, .5, .5)
        if(!blockExplosion) {
            event.isCancelled = true
            world.createExplosion(location, event.yield, false, false)
        }
    }

    @EventHandler
    fun explosionDamage(event : EntityDamageByEntityEvent) {
        if(explosionDamage) return
        if(event.cause != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION &&
            event.cause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return
        event.isCancelled = true
    }
    @EventHandler
    fun explosionDamage(event : EntityDamageByBlockEvent) {
        if(explosionDamage) return
        if(event.cause != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION &&
            event.cause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return
        event.isCancelled = true
    }
}