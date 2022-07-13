/*
 * Author: Jonathan Haimann
 * Version: 1.1
 * Date:    07.07.2022
 */

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Enemy {
    private Random random;
    private int level;
    private Enemytype type;
    private int health;
    private int attack;
    private int defense;
    private boolean firstAttack;
    private int Xp;

    enum Enemytype {
        OwlbearSkeleton,
        WolfSkeleton,
        BugbearZombie,
        HumanZombie,
        Ghoul
    }

    /**
     * Create an Enemy object. The Level end the type of the enemy must be supplied
     * Usage - Enemy(2, Enemy.Enemytype.Ghoul);
     * 
     * @param level - int
     * @param type  - Enemy.Enemytype
     * @throws Exception
     */
    public Enemy(int level, Enemytype type) {
        this.type = type;
        this.level = level;
        firstAttack = true;
        switch (type) {
            case OwlbearSkeleton:
                health = 50 * level;
                attack = 20 * level;
                defense = 10 * level;
                break;
            case WolfSkeleton:
                health = 20 * level;
                attack = 10 * level;
                defense = 10 * level;
                break;
            case BugbearZombie:
                health = 100 * level;
                attack = 10 * level;
                defense = 20 * level;
                break;
            case HumanZombie:
                health = 10 * level;
                attack = 10 * level;
                defense = 10 * level;
                break;
            case Ghoul:
                health = 50 * level;
                attack = 10 * level;
                defense = 10 * level + 1;
                break;
        }
    }

    /**
     * Return a random number between a lower and an upper bound
     * Usage - getRandomNumber(2, 10);
     * 
     * @param lowerBound - int
     * @param upperBound - int
     * @return - int
     * @throws Exception
     */
    private int getRandomNumber(int lowerBound, int upperBound) {
        int randomNumber = ThreadLocalRandom.current().nextInt(lowerBound, upperBound + 1);
        return randomNumber;
    }

    /**
     * Return a random string from an array of attackmoves. the type is selected
     * automatically
     * Usage - selectRandomAttackMove();
     * 
     * @return - String
     * @throws Exception
     */
    private String selectRandomAttackMove() {
        random = new Random();
        switch (type) {
            case OwlbearSkeleton:
                String attackMovesO[] = {
                        "With its huge claws it wildly slashes in your direction",
                        "It snaps at you with its peak",
                        "It slams into you"
                };
                return attackMovesO[random.nextInt(attackMovesO.length)];
            case WolfSkeleton:
                String attackMovesW[] = {
                        "Its teeth snap at you",
                        "It tries to scartch you",
                        "it circles around you and pounces"
                };
                return attackMovesW[random.nextInt(attackMovesW.length)];
            case BugbearZombie:
                String attackMovesB[] = {
                        "It swings at you with its axe",
                        "It screams wiledly and slams into you",
                        "It grabs you with a hand and wants to throw you at the wall"
                };
                return attackMovesB[random.nextInt(attackMovesB.length)];
            case HumanZombie:
                String attackMovesH[] = {
                        "It tries to byte you"
                };
                return attackMovesH[random.nextInt(attackMovesH.length)];
            case Ghoul:
                String attackMovesG[] = {
                        "It screams: 'I am HUUUUNGRY!!!!' and jumps at you",
                        "With a finger it points to the right while trying to attack from the left",
                        "It tries to shake your hand as a token of friendship. As soon as you try to grab it the "
                                + type.toString() + " tries to bite of your hand",
                        "It stares at you, you stare back. You feel a sharp pain inside you."
                };
                return attackMovesG[random.nextInt(attackMovesG.length)];
        }
        return "";
    }

    /**
     * Return the type of the enemy
     * Usage - getType();
     * 
     * @return - Enemy.Enemytype
     * @throws Exception
     */
    public Enemytype getType() {
        return type;
    }

    /**
     * Return the health of the enemy
     * Usage - getHealth();
     * 
     * @return - int
     * @throws Exception
     */
    public int getHealth() {
        return health;
    }

    /**
     * Return the attack stats of the enemy
     * Usage - getAttack();
     * 
     * @return - int
     * @throws Exception
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Return the attack damage of the enemy after it was probed against the
     * defenders defence
     * Usage - attack(10);
     * 
     * @param defenseToOvercome - int
     * @return - int
     * @throws Exception
     */
    public int attack(int defenseToOvercome) {
        int hitchance = getRandomNumber(level, attack);
        if (hitchance > defenseToOvercome) {
            return attack;
        } else {
            return 0;
        }
    }

    /**
     * Return the attack damage done after it was probed against the monsters
     * defence
     * Usage - defend(10);
     * 
     * @param attackToParry - int
     * @return - int
     * @throws Exception
     */
    public int defend(int attackToParry) {
        int damage = 0;
        int parrychance = getRandomNumber(level, defense);
        if (parrychance < attackToParry) {
            damage = attackToParry - parrychance;
            health = health - damage;
        }
        return damage;
    }

    /**
     * Return the level of the monster
     * Usage - getLevel();
     * 
     * @return - int
     * @throws Exception
     */
    public int getLevel() {
        return level;
    }
    public int getXp(int roundsTillDefeated){
        int xp = level * (30 - roundsTillDefeated);
        if (xp <=10){
            return 10;
        }
        else {
            return xp;
        }
    }


    /**
     * Return the attackmove of the monster
     * Usage - getAttackMove();
     * 
     * @return - String
     * @throws Exception
     */
    public String getAttackMove() {
        String returnString = "";
        switch (type) {
            case OwlbearSkeleton:
                if (firstAttack) {
                    firstAttack=false;
                    returnString = "The " + type.toString() + " runs towards you.\n";
                }
                break;
            case WolfSkeleton:
                if (firstAttack) {
                    firstAttack=false;
                    returnString = "The " + type.toString() + " runs towards you.\n";
                }
                break;
            case BugbearZombie:
                if (firstAttack) {
                    firstAttack=false;
                    returnString = "The " + type.toString()
                            + " walks towards you. It wears an iron armor and weilds a huge axe\n";
                }
                break;
            case HumanZombie:
                if (firstAttack) {
                    firstAttack=false;
                    returnString = "The " + type.toString()
                            + " slowly walks towards you. You have the impression that you know the person it once was\n";
                }
                break;
            case Ghoul:
                if (firstAttack) {
                    firstAttack=false;
                    returnString = "The " + type.toString()
                            + " runds towards you. It seems to have some inteligence but is overwhelmed by its hunger\n";
                }
                break;
        }
        return returnString + selectRandomAttackMove();
    }
}
