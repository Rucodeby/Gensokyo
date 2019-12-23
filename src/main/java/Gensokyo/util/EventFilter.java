package Gensokyo.util;

import Gensokyo.GensokyoMod;
import Gensokyo.events.ClashOfLegends;
import Gensokyo.events.DemonBookSeller;
import Gensokyo.events.FieldTripToAnotherWorld;
import Gensokyo.events.ForestOfMagic;
import Gensokyo.events.GardenOfTheSun;
import Gensokyo.events.GoodsFromTheOutsideWorld;
import Gensokyo.events.HakureiShrine;
import Gensokyo.events.ScarletDevilMansion;
import Gensokyo.events.marisaEvents.AnOldGhost;
import Gensokyo.events.marisaEvents.BookThief;
import Gensokyo.events.marisaEvents.JustAVisit;
import ThMod.characters.Marisa;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.HappyFlower;

import java.util.ArrayList;

public class EventFilter {

    //Not supposed to be instantiated
    private EventFilter() {
        throw new AssertionError();
    }

    public static ArrayList<String> FilterEvents(ArrayList<String> events) {
        ArrayList<String> eventsToRemove = new ArrayList<>();
        for (String event : events) {
            if (event.equals(DemonBookSeller.ID)) {
                if (!(AbstractDungeon.player.gold >= DemonBookSeller.COST)) {
                    eventsToRemove.add(event);
                }
            } else if (event.equals(GoodsFromTheOutsideWorld.ID)) {
                if (!(AbstractDungeon.player.gold >= GoodsFromTheOutsideWorld.commonCost)) {
                    eventsToRemove.add(event);
                }
            } else if (event.equals(HakureiShrine.ID)) {
                if (!(AbstractDungeon.player.gold >= HakureiShrine.upgradeCost || (AbstractDungeon.player.currentHealth < AbstractDungeon.player.maxHealth && AbstractDungeon.player.gold >= 2))) {
                    eventsToRemove.add(event);
                }
            } else if (event.equals(ClashOfLegends.ID)) {
                if (!(AbstractDungeon.player.maxHealth > 1)) {
                    eventsToRemove.add(event);
                }
            } else if (event.equals(GardenOfTheSun.ID)) {
                if (AbstractDungeon.player.hasRelic(HappyFlower.ID) || !GardenOfTheSun.hasNonStrikeAttack()) {
                    eventsToRemove.add(event);
                }
            } else if (event.equals(FieldTripToAnotherWorld.ID)) {
                if (!FieldTripToAnotherWorld.hasNonBasicCard()) {
                    eventsToRemove.add(event);
                }
            } else if (event.equals(JustAVisit.ID)) {
                if (!(AbstractDungeon.player.gold >= JustAVisit.COST)) {
                    eventsToRemove.add(event);
                }
            }
            if (MarisaSpecificFilter(event)) {
                eventsToRemove.add(event);
            }
        }
        return eventsToRemove;
    }

    private static boolean MarisaSpecificFilter(String event) {
        //Filter out these events if the player is not Marisa
        if (GensokyoMod.hasMarisa && !(AbstractDungeon.player instanceof Marisa)) {
            if (event.equals(AnOldGhost.ID)) {
                return true;
            } else if (event.equals(JustAVisit.ID)) {
                return true;
            } else if (event.equals(BookThief.ID)) {
                return true;
            }
            //Filter out these events if the player is Marisa
        } else if (GensokyoMod.hasMarisa && AbstractDungeon.player instanceof Marisa) {
            if (event.equals(ScarletDevilMansion.ID)) {
                return true;
            } else if (event.equals(ForestOfMagic.ID)) {
                return true;
            } else if (event.equals(HakureiShrine.ID)) {
                return true;
            } else if (event.equals(GoodsFromTheOutsideWorld.ID)) {
                return true;
            }
        }
        return false;
    }
}
