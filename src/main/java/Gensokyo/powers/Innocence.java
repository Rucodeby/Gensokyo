package Gensokyo.powers;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.GensokyoMod;
import Gensokyo.actions.BalanceShiftAction;
import Gensokyo.monsters.bossRush.Eiki;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Gensokyo.GensokyoMod.makePowerPath;

public class Innocence extends AbstractPower {

    public static final String POWER_ID = GensokyoMod.makeID("Innocence");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private Eiki eiki;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("DeathTouch84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("DeathTouch32.png"));

    public Innocence(AbstractCreature owner, int amount, Eiki eiki) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.eiki = eiki;

        type = PowerType.BUFF;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.owner != this.owner && info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0) {
            this.flash();
            this.amount++;
            updateDescription();
            eiki.innocence.add(new BetterSpriterAnimation("GensokyoResources/images/monsters/Eiki/Innocence/Spriter/InnocenceAnimation.scml"));
            AbstractDungeon.actionManager.addToBottom(new BalanceShiftAction(this.eiki));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
