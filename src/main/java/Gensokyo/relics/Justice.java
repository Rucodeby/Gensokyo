package Gensokyo.relics;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Gensokyo.GensokyoMod.makeRelicOutlinePath;
import static Gensokyo.GensokyoMod.makeRelicPath;

public class Justice extends CustomRelic {

    public static final String ID = GensokyoMod.makeID("Justice");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Justice.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Justice.png"));

    private boolean dealDamage = false;
    private static int TRIGGER_COUNT = 2;
    private static int DAMAGE = 6;

    public Justice() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onPlayerEndTurn() {
        if (this.dealDamage) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(DAMAGE, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
        this.dealDamage = false;
        this.pulse = false;
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        this.counter = 0;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK && !dealDamage) {
            ++this.counter;
            if (this.counter % TRIGGER_COUNT == 0) {
                this.beginPulse();
                this.pulse = true;
                this.dealDamage = true;
            }
        }
    }

    public void onVictory() {
        this.counter = -1;
        this.pulse = false;
        this.dealDamage = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TRIGGER_COUNT + DESCRIPTIONS[1] + DAMAGE + DESCRIPTIONS[2];
    }

}