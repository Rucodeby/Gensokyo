package Gensokyo.monsters;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.DeathMark;
import Gensokyo.powers.Vengeance;
import Gensokyo.relics.CelestialsFlawlessClothing;
import basemod.abstracts.CustomMonster;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Sumireko extends CustomMonster
{
    public static final String ID = "Gensokyo:Sumireko";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private boolean firstMove = true;
    private boolean secondMove = true;
    private Intent scytheIntent = Intent.ATTACK;
    private static final byte SCYTHE = 1;
    private static final byte DEBUFF = 2;
    private static final byte DEATH = 3;
    private static final int SCYTHE_DAMAGE = 15;
    private static final int SCYTHE_ACT_DAMAGE_BONUS = 5;
    private static final int DEATH_COUNTER = 7;
    private static final int A_2_SCYTHE_DAMAGE = 18;
    private static final int A_2_DEATH_COUNTER = 6;
    private int scytheDmg;
    private int deathCounter;
    private static final int WEAK_AMT = 2;
    private static final int FRAIL_AMT = 2;
    private static float actMultiplier = 0.0f;
    private static final float ACT_1_MULTIPLIER = 1.0f;
    private static final float ACT_2_MULTIPLIER = 1.5f;
    private static final float ACT_3_MULTIPLIER = 2.0f;
    private static final float ACT_4_MULTIPLIER = 2.5f;
    private static final int HP_MIN = 80;
    private static final int HP_MAX = 84;
    private static final int A_2_HP_MIN = 84;
    private static final int A_2_HP_MAX = 90;

    public Sumireko() {
        this(0.0f, 0.0f);
    }

    public Sumireko(final float x, final float y) {
        super(Sumireko.NAME, ID, HP_MAX, -5.0F, 0, 220.0f, 255.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Sumireko/Spriter/SumirekoAnimation.scml");
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.actNum == 1) {
            actMultiplier = ACT_1_MULTIPLIER;
        } else if (AbstractDungeon.actNum == 2) {
            actMultiplier = ACT_2_MULTIPLIER;
        } else if (AbstractDungeon.actNum == 3) {
            actMultiplier = ACT_3_MULTIPLIER;
        } else {
            actMultiplier = ACT_4_MULTIPLIER;
        }
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp((int)(A_2_HP_MIN * actMultiplier), (int)(A_2_HP_MAX * actMultiplier));
        }
        else {
            this.setHp((int)(HP_MIN * actMultiplier), (int)(HP_MAX * actMultiplier));
        }
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.scytheDmg = A_2_SCYTHE_DAMAGE + (SCYTHE_ACT_DAMAGE_BONUS * (AbstractDungeon.actNum - 1));
            this.deathCounter = A_2_DEATH_COUNTER;
        }
        else {
            this.scytheDmg = SCYTHE_DAMAGE + (SCYTHE_ACT_DAMAGE_BONUS * (AbstractDungeon.actNum - 1));
            this.deathCounter = DEATH_COUNTER;
        }
        this.damage.add(new DamageInfo(this, this.scytheDmg));

        Player.PlayerListener listener = new SumirekoListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1: {
                //runAnim("Scythe");
                if (scytheIntent == Intent.ATTACK_DEBUFF) {
                    for (AbstractPower power : AbstractDungeon.player.powers) {
                        if (power.type == AbstractPower.PowerType.BUFF) {
                            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, power.ID));
                        }
                    }
                }
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            }
            case 2: {
                //runAnim("SpellB");
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, WEAK_AMT, true), WEAK_AMT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, FRAIL_AMT, true), FRAIL_AMT));
                break;
            }
            case 3: {
                //runAnim("SpellB");
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DeathMark(AbstractDungeon.player, deathCounter), 0));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove && !AbstractDungeon.player.hasPower(DeathMark.POWER_ID)) {
            this.setMove(Sumireko.MOVES[2], DEATH, Intent.STRONG_DEBUFF);
            this.firstMove = false;
        } else if (this.secondMove){
            this.setMove(Sumireko.MOVES[1], DEBUFF, Intent.DEBUFF);
            this.secondMove = false;
        } else {
            if (this.lastTwoMoves(SCYTHE)) {
                this.setMove(Sumireko.MOVES[1], DEBUFF, Intent.DEBUFF);
            } else {
                this.setMove(Sumireko.MOVES[0], SCYTHE, scytheIntent, (this.damage.get(0)).base);
            }
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Sumireko");
        NAME = Sumireko.monsterStrings.NAME;
        MOVES = Sumireko.monsterStrings.MOVES;
        DIALOG = Sumireko.monsterStrings.DIALOG;
    }

    @Override
    public void die(boolean triggerRelics) {
        //runAnim("Defeat");
        super.die(triggerRelics);
    }

    //Runs a specific animation
    public void runAnim(String animation) {
        ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation(animation);
    }

    //Resets character back to idle animation
    public void resetAnimation() {
        ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation("Idle");
    }

    //Prevents any further animation once the death animation is finished
    public void stopAnimation() {
        int time = ((BetterSpriterAnimation)this.animation).myPlayer.getAnimation().length;
        ((BetterSpriterAnimation)this.animation).myPlayer.setTime(time);
        ((BetterSpriterAnimation)this.animation).myPlayer.speed = 0;
    }

    public class SumirekoListener implements Player.PlayerListener {

        private Sumireko character;

        public SumirekoListener(Sumireko character) {
            this.character = character;
        }

        public void animationFinished(Animation animation){
            if (animation.name.equals("Defeat")) {
                character.stopAnimation();
            } else if (!animation.name.equals("Idle")) {
                character.resetAnimation();
            }
        }

        //UNUSED
        public void animationChanged(Animation var1, Animation var2){

        }

        //UNUSED
        public void preProcess(Player var1){

        }

        //UNUSED
        public void postProcess(Player var1){

        }

        //UNUSED
        public void mainlineKeyChanged(com.brashmonkey.spriter.Mainline.Key var1, com.brashmonkey.spriter.Mainline.Key var2){

        }
    }
}