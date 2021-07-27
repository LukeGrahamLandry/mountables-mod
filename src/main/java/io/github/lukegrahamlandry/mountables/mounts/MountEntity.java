package io.github.lukegrahamlandry.mountables.mounts;

import io.github.lukegrahamlandry.mountables.config.MountsConfig;
import io.github.lukegrahamlandry.mountables.init.ItemInit;
import io.github.lukegrahamlandry.mountables.init.MountTypes;
import io.github.lukegrahamlandry.mountables.items.MountSummonItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MountEntity extends CreatureEntity implements IJumpingMount{
    private static final DataParameter<Integer> TEXTURE = EntityDataManager.defineId(MountEntity.class, DataSerializers.INT);
    private static final DataParameter<String> VANILLA_TYPE = EntityDataManager.defineId(MountEntity.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> CAN_FLY = EntityDataManager.defineId(MountEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_BABY = EntityDataManager.defineId(MountEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.defineId(MountEntity.class, DataSerializers.OPTIONAL_UUID);
    private static final DataParameter<Integer> COLOR = EntityDataManager.defineId(MountEntity.class, DataSerializers.INT);

    private static final DataParameter<Boolean> HAS_FIRE_CORE = EntityDataManager.defineId(MountEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAS_WATER_CORE = EntityDataManager.defineId(MountEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> SPEED_CORES = EntityDataManager.defineId(MountEntity.class, DataSerializers.INT);

    public static final int maxHealth = 20;

    private ItemStack summonStack;
    private EntityType vanillaType;

    public float targetSquish = 1;
    public float squish = 1;
    public float oSquish;
    boolean wasOnGround = true;

    public void setMountType(ItemStack stack){
        this.summonStack = stack.copy();
        this.vanillaType = MountSummonItem.getType(stack);
        int texture = stack.getTag().getInt("texturetype");
        this.setTextureType(texture);
        int health = stack.getTag().getInt("health");
        this.setHealth(health);
        this.entityData.set(VANILLA_TYPE, EntityType.getKey(this.vanillaType).toString());
        this.entityData.set(CAN_FLY, stack.getTag().getBoolean("canfly"));
        this.setChild(stack.getTag().getBoolean("baby"));
        int color = stack.getTag().getInt("colortype");
        this.setColorType(color);
    }

    @Override
    public void onSyncedDataUpdated(DataParameter<?> p_184206_1_) {
        if (IS_BABY.equals(p_184206_1_)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(p_184206_1_);
    }

    @Override
    public EntitySize getDimensions(Pose p_213305_1_) {
        return this.getVanillaType().getDimensions().scale(this.getScale());
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        setStanding(false);
        this.refreshDimensions();

        // hopefully fix crazy dying with one probe bug
        if (this.getHealth() < 3){
            this.heal(2);
        }
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!level.isClientSide()){
            this.summonStack = writeToStack();
            this.spawnAtLocation(summonStack);
        }
    }

    @Override
    public boolean fireImmune() {
        return this.entityData.get(HAS_FIRE_CORE);
    }

    @Override
    public void setCustomName(@Nullable ITextComponent text) {
        super.setCustomName(text);
        if (this.summonStack != null) this.summonStack.setHoverName(text);
    }

    @Override
    public void checkDespawn() {
        // super.checkDespawn();
    }

    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        if (this.isVehicle()) {
            return super.mobInteract(player, hand);
        }

        ItemStack itemstack = player.getItemInHand(hand);
        if (!itemstack.isEmpty()) {
            boolean textureSuccess = MountTextureUtil.tryUpdateTexture(this, itemstack);
            if (!textureSuccess) textureSuccess = MountTextureUtil.tryUpdateColor(this, itemstack);

            if (!textureSuccess){
                boolean coreSuccess = false;
                if (itemstack.getItem() == ItemInit.FLIGHT_CORE.get()) {
                    this.entityData.set(CAN_FLY, true);
                    coreSuccess = true;
                }
                if (itemstack.getItem() == ItemInit.FIRE_CORE.get()) {
                    this.entityData.set(HAS_FIRE_CORE, true);
                    coreSuccess = true;
                }
                if (itemstack.getItem() == ItemInit.WATER_CORE.get()) {
                    this.entityData.set(HAS_WATER_CORE, true);
                    coreSuccess = true;
                }
                if (itemstack.getItem() == ItemInit.SPEED_CORE.get()) {
                    this.entityData.set(SPEED_CORES, this.entityData.get(SPEED_CORES) + 1);
                    coreSuccess = true;
                }
                if (itemstack.getItem() == ItemInit.SLOW_CORE.get()) {
                    this.entityData.set(SPEED_CORES, this.entityData.get(SPEED_CORES) - 1);
                    coreSuccess = true;
                }
                if (itemstack.getItem() == ItemInit.RESET_CORE.get()) {
                    if (!MountSummonItem.canFlyByDefault(this.getVanillaType())){
                        this.entityData.set(CAN_FLY, false);
                    }
                    this.entityData.set(HAS_WATER_CORE, false);
                    this.entityData.set(HAS_FIRE_CORE, false);
                    this.entityData.set(SPEED_CORES, 0);
                    coreSuccess = true;
                }

                if (coreSuccess){
                    itemstack.shrink(1);
                    if (!level.isClientSide()) doParticles(ParticleTypes.HAPPY_VILLAGER);
                    return ActionResultType.sidedSuccess(this.level.isClientSide);
                }
            }

            if (textureSuccess){
                if (MountsConfig.doesTextureSwapConsume()){
                    if (itemstack.getItem() == Items.WATER_BUCKET) player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                    else if (itemstack.getItem() != Items.SHEARS) itemstack.shrink(1);
                }

                if (!level.isClientSide()) doParticles(ParticleTypes.HAPPY_VILLAGER);
                return ActionResultType.sidedSuccess(this.level.isClientSide);
            }

            if (itemstack.getItem() == Items.MILK_BUCKET && allowBaby()){
                this.setChild(!this.isBaby());
                if (!level.isClientSide()) doParticles(ParticleTypes.HAPPY_VILLAGER);
                if (!player.isCreative()) player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                return ActionResultType.sidedSuccess(this.level.isClientSide);
            }

            // eat food
            Food food = itemstack.getItem().getFoodProperties();
            if (food != null){
                if (!level.isClientSide()){
                    // regen
                    int toHeal = Math.floorDiv(food.getNutrition(), 3);
                    if (toHeal >= 1){
                        this.heal(toHeal);
                    }
                    // effects
                    food.getEffects().forEach((pair) -> {
                        if (pair.getFirst() != null && random.nextFloat() < pair.getSecond()) this.addEffect(pair.getFirst());
                    });

                    doParticles(ParticleTypes.HEART);
                }
                // take
                itemstack.shrink(1);
                return ActionResultType.CONSUME;
            }

            ActionResultType actionresulttype = itemstack.interactLivingEntity(player, this, hand);
            if (actionresulttype.consumesAction()) {
                return actionresulttype;
            }
        }

        if (player.isShiftKeyDown() && isOwner(player) && player.getItemInHand(hand).isEmpty() && hand == Hand.MAIN_HAND){
            if (!level.isClientSide()){
                // crashes on client if loaded from nbt because stack is null
                this.summonStack = this.writeToStack();
                player.setItemInHand(hand, this.summonStack);
            }
            this.remove();
            return ActionResultType.SUCCESS;
        }

        this.doPlayerRide(player);
        return ActionResultType.sidedSuccess(this.level.isClientSide);
    }

    private boolean allowBaby() {
        return canBeBaby(this.vanillaType);
    }

    public static boolean canBeBaby(EntityType vanillaType) {
        return vanillaType == EntityType.COW || vanillaType == EntityType.PIG || vanillaType == EntityType.SHEEP
                || vanillaType == EntityType.WOLF || vanillaType == EntityType.FOX || vanillaType == EntityType.CAT
                || vanillaType == EntityType.LLAMA || vanillaType == EntityType.PANDA || vanillaType == EntityType.ZOMBIE
                || vanillaType == EntityType.WITHER || vanillaType == EntityType.GHAST || vanillaType == EntityType.HOGLIN
                || vanillaType == EntityType.TURTLE || vanillaType == EntityType.CHICKEN || vanillaType == EntityType.SLIME
                || vanillaType == EntityType.MAGMA_CUBE;
    }

    private void doParticles(BasicParticleType particle) {
        float width = this.getDimensions(this.getPose()).width;
        double yShift = this.getDimensions(this.getPose()).height * random.nextDouble();
        for(int i = 0; i < 7; ++i) {
            ((ServerWorld)level).sendParticles(particle, getX() + (random.nextDouble() * (width * 2) - width), getY() + yShift, getZ() + (random.nextDouble() * (width * 2) - width), 1, 0.0D, 0.0D, 0.0D, 1.0D);
        }
    }

    private boolean isOwner(PlayerEntity player) {
        return this.getOwnerUUID() != null && player.getUUID().toString().equals(this.getOwnerUUID().toString());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(1, new FollowGoal(this, 1.25, 10, 2, false));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TEXTURE, 0);
        this.entityData.define(COLOR, 0);
        this.entityData.define(VANILLA_TYPE, "");
        this.entityData.define(CAN_FLY, false);
        this.entityData.define(IS_BABY, false);
        this.entityData.define(OWNER, Optional.empty());

        this.entityData.define(HAS_WATER_CORE, false);
        this.entityData.define(HAS_FIRE_CORE, false);
        this.entityData.define(SPEED_CORES, 0);
    }

    public void setTextureType(int x){
        this.entityData.set(TEXTURE, x);
    }

    public int getTextureType(){
        return this.entityData.get(TEXTURE);
    }

    public void setColorType(int x){
        this.entityData.set(COLOR, x);
    }

    public int getColorType(){
        return this.entityData.get(COLOR);
    }

    public EntityType getVanillaType() {
        return EntityType.byString(this.entityData.get(VANILLA_TYPE)).orElse(EntityType.CREEPER);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        this.summonStack = writeToStack();
        nbt.put("summon", this.summonStack.getTag());
        nbt.putUUID("owner", getOwnerUUID());
    }

    public ItemStack writeToStack(){
        if (this.summonStack == null) this.summonStack = new ItemStack(ItemInit.MOUNT_SUMMON.get());
        MountSummonItem.writeNBT(this.summonStack, this.vanillaType, this.getTextureType(), (int) this.getHealth(), this.canFly(), this.isBaby(), this.getColorType(), this.entityData.get(HAS_FIRE_CORE), this.entityData.get(HAS_FIRE_CORE), this.entityData.get(SPEED_CORES));
        return this.summonStack;
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        ItemStack stack = new ItemStack(ItemInit.MOUNT_SUMMON.get());
        stack.setTag(nbt.getCompound("summon"));
        this.setMountType(stack);
        this.setOwnerUUID(nbt.getUUID("owner"));
    }

    private boolean canFly() {
        return this.entityData.get(CAN_FLY) && (this.level.isClientSide() || MountsConfig.isFlightAllowed());
    }

    public void setOwnerUUID(UUID id) {
        this.entityData.set(OWNER, Optional.of(id));
    }

    public LivingEntity getOwner() {
       if (getOwnerUUID() == null) return null;
       return (LivingEntity) ((ServerWorld)level).getEntity(getOwnerUUID());
    }

    public UUID getOwnerUUID() {
        if (!this.entityData.get(OWNER).isPresent()) return null;
        return this.entityData.get(OWNER).get();
    }

    @Override
    public boolean isBaby() {
        return this.entityData.get(IS_BABY);
    }

    public void setChild(boolean x) {
        this.entityData.set(IS_BABY, x);
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return this.entityData.get(HAS_WATER_CORE) || (this.getVanillaType() == EntityType.SQUID || this.getVanillaType() == EntityType.TURTLE || this.getVanillaType() == EntityType.GUARDIAN);
    }

    public boolean isSlowOnLand() {
        return !this.entityData.get(HAS_WATER_CORE) && (this.getVanillaType() == EntityType.SQUID || this.getVanillaType() == EntityType.TURTLE || this.getVanillaType() == EntityType.GUARDIAN);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return this.canBeRiddenInWater(null);
    }

    public EntityType<?> getType() {
        //if (this.isBaby()){
          //  return (this.getVanillaType() == EntityType.SLIME && this.getTextureType() == 1) ? MountTypes.babyMountLookup.get(EntityType.MAGMA_CUBE).get() : MountTypes.babyMountLookup.get(getVanillaType()).get();
        //}
        return (this.getVanillaType() == EntityType.SLIME && this.getTextureType() == 1) ? MountTypes.get(EntityType.MAGMA_CUBE).getType() : super.getType();
    }

    public MountEntity(EntityType<? extends CreatureEntity> p_i48567_1_, World p_i48567_2_) {
        super(p_i48567_1_, p_i48567_2_);
        this.maxUpStep = 1;
    }

    private int standCounter;
    public int tailCounter;
    public int sprintCounter;
    protected boolean isJumping;
    protected float playerJumpPendingScale;
    private boolean allowStandSliding;
    private float eatAnim;
    protected int gallopSoundCounter;
    private boolean standing = true;

    public boolean isJumping() {
        return this.isJumping;
    }

    public void setIsJumping(boolean p_110255_1_) {
        this.isJumping = p_110255_1_;
    }

    public boolean isStanding() {
        return this.standing;
    }

    public boolean isSaddled() {
        return true;
    }


    public boolean isPushable() {
        return !this.isVehicle();
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        if (p_225503_1_ > 1.0F) {
            // this.playSound(SoundEvents.HORSE_LAND, 0.4F, 1.0F);
        }

        int i = this.calculateFallDamage(p_225503_1_, p_225503_2_);
        if (i <= 0 || this.canFly() || this.getVanillaType() == EntityType.SLIME) {
            return false;
        } else {
            this.hurt(DamageSource.FALL, (float)i);
            if (this.isVehicle()) {
                for(Entity entity : this.getIndirectPassengers()) {
                    entity.hurt(DamageSource.FALL, (float)i);
                }
            }

            this.playBlockFallSound();
            return true;
        }
    }

    protected int calculateFallDamage(float p_225508_1_, float p_225508_2_) {
        return MathHelper.ceil((p_225508_1_ * 0.5F - 3.0F) * p_225508_2_);
    }

    public double getCustomJump() {
        return 0.65D;// this.getAttributeValue(Attributes.JUMP_STRENGTH);
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        if (this.random.nextInt(3) == 0) {
            this.stand();
        }

        return null;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.random.nextInt(10) == 0 && !this.isImmobile()) {
            this.stand();
        }

        if (this.getVanillaType() == EntityType.BEE) return SoundEvents.BEE_LOOP;
        if (this.getVanillaType() == EntityType.SHEEP) return SoundEvents.SHEEP_AMBIENT;
        if (this.getVanillaType() == EntityType.PIG) return SoundEvents.PIG_AMBIENT;
        if (this.getVanillaType() == EntityType.COW) return SoundEvents.COW_AMBIENT;
        if (this.getVanillaType() == EntityType.WOLF) return SoundEvents.WOLF_AMBIENT;
        if (this.getVanillaType() == EntityType.SNOW_GOLEM) return SoundEvents.SNOW_GOLEM_AMBIENT;
        if (this.getVanillaType() == EntityType.CHICKEN) return SoundEvents.CHICKEN_AMBIENT;
        if (this.getVanillaType() == EntityType.SPIDER) return SoundEvents.SPIDER_AMBIENT;
        if (this.getVanillaType() == EntityType.CREEPER) return null;
        if (this.getVanillaType() == EntityType.CAT) return SoundEvents.CAT_AMBIENT;
        if (this.getVanillaType() == EntityType.LLAMA) return SoundEvents.LLAMA_AMBIENT;
        if (this.getVanillaType() == EntityType.FOX) return SoundEvents.FOX_AMBIENT;
        if (this.getVanillaType() == EntityType.PANDA) return SoundEvents.PANDA_AMBIENT;
        if (this.getVanillaType() == EntityType.ZOMBIE) return SoundEvents.ZOMBIE_AMBIENT;
        if (this.getVanillaType() == EntityType.SKELETON) return SoundEvents.SKELETON_AMBIENT;
        if (this.getVanillaType() == EntityType.PHANTOM) return SoundEvents.PHANTOM_AMBIENT;
        if (this.getVanillaType() == EntityType.GHAST) return SoundEvents.GHAST_AMBIENT;
        if (this.getVanillaType() == EntityType.SLIME) return null;
        if (this.getVanillaType() == EntityType.WITHER) return SoundEvents.WITHER_AMBIENT;
        if (this.getVanillaType() == EntityType.HOGLIN) return SoundEvents.HOGLIN_AMBIENT;
        if (this.getVanillaType() == EntityType.RAVAGER) return SoundEvents.RAVAGER_AMBIENT;
        if (this.getVanillaType() == EntityType.TURTLE) return this.isInWaterOrBubble() ? null : SoundEvents.TURTLE_AMBIENT_LAND;
        if (this.getVanillaType() == EntityType.SQUID) return SoundEvents.SQUID_AMBIENT;
        if (this.getVanillaType() == EntityType.GUARDIAN) return this.isInWaterOrBubble() ? SoundEvents.GUARDIAN_AMBIENT : SoundEvents.GUARDIAN_AMBIENT_LAND;

        return null;
    }

    @Nullable
    protected SoundEvent getAngrySound() {
        this.stand();
        return null;
    }

    @Nullable
    protected SoundEvent getStepSound() {
        if (this.getVanillaType() == EntityType.BEE) return null;
        if (this.getVanillaType() == EntityType.SHEEP) return SoundEvents.SHEEP_STEP;
        if (this.getVanillaType() == EntityType.PIG) return SoundEvents.PIG_STEP;
        if (this.getVanillaType() == EntityType.COW) return SoundEvents.COW_STEP;
        if (this.getVanillaType() == EntityType.WOLF) return SoundEvents.WOLF_STEP;
        if (this.getVanillaType() == EntityType.SNOW_GOLEM) return null;
        if (this.getVanillaType() == EntityType.CHICKEN) return SoundEvents.CHICKEN_STEP;
        if (this.getVanillaType() == EntityType.SPIDER) return SoundEvents.SPIDER_STEP;
        if (this.getVanillaType() == EntityType.CREEPER) return null;
        if (this.getVanillaType() == EntityType.CAT) return null;
        if (this.getVanillaType() == EntityType.LLAMA) return SoundEvents.LLAMA_STEP;
        if (this.getVanillaType() == EntityType.FOX) return null;
        if (this.getVanillaType() == EntityType.PANDA) return SoundEvents.PANDA_STEP;
        if (this.getVanillaType() == EntityType.ZOMBIE) return SoundEvents.ZOMBIE_STEP;
        if (this.getVanillaType() == EntityType.SKELETON) return SoundEvents.SKELETON_STEP;
        if (this.getVanillaType() == EntityType.PHANTOM) return null;
        if (this.getVanillaType() == EntityType.GHAST) return null;
        if (this.getVanillaType() == EntityType.SLIME) return SoundEvents.SLIME_BLOCK_STEP;
        if (this.getVanillaType() == EntityType.WITHER) return null;
        if (this.getVanillaType() == EntityType.HOGLIN) return SoundEvents.HOGLIN_STEP;
        if (this.getVanillaType() == EntityType.RAVAGER) return SoundEvents.RAVAGER_STEP;
        if (this.getVanillaType() == EntityType.TURTLE) return this.isInWaterOrBubble() ? SoundEvents.TURTLE_SWIM : SoundEvents.TURTLE_SHAMBLE;
        if (this.getVanillaType() == EntityType.SQUID) return null;
        if (this.getVanillaType() == EntityType.GUARDIAN) return null;

        return null;
    }

    @Override
    protected SoundEvent getSwimSound() {
        if (this.getVanillaType() == EntityType.TURTLE) return SoundEvents.TURTLE_SWIM;
        if (this.getVanillaType() == EntityType.HOGLIN) return SoundEvents.HOSTILE_SWIM;
        return SoundEvents.GENERIC_SWIM;
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        if (this.getStepSound() == null) return;
        if (!p_180429_2_.getMaterial().isLiquid()) {
            BlockState blockstate = this.level.getBlockState(p_180429_1_.above());
            SoundType soundtype = p_180429_2_.getSoundType(level, p_180429_1_, this);
            if (blockstate.is(Blocks.SNOW)) {
                soundtype = blockstate.getSoundType(level, p_180429_1_, this);
            }

            // should i shift based on the soundtype idk

            this.playSound(this.getStepSound(), 0.15F, 1.0F);

            /* // horse
            if (this.isVehicle() && this.canGallop) {
                ++this.gallopSoundCounter;
                if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
                    this.playGallopSound(soundtype);
                } else if (this.gallopSoundCounter <= 5) {
                    this.playSound(SoundEvents.HORSE_STEP_WOOD, soundtype.getVolume() * 0.15F, soundtype.getPitch());
                }
            } else if (soundtype == SoundType.WOOD) {
                this.playSound(SoundEvents.HORSE_STEP_WOOD, soundtype.getVolume() * 0.15F, soundtype.getPitch());
            } else {
                this.playSound(SoundEvents.HORSE_STEP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
            }

             */

        }


    }

    protected void playGallopSound(SoundType p_190680_1_) {
        // this.playSound(SoundEvents.HORSE_GALLOP, p_190680_1_.getVolume() * 0.15F, p_190680_1_.getPitch());
    }

    public int getMaxSpawnClusterSize() {
        return 6;
    }

    protected float getSoundVolume() {
        return 0.8F;
    }

    public int getAmbientSoundInterval() {
        return 350 + random.nextInt(100);
    }

    protected void doPlayerRide(PlayerEntity player) {
        this.setStanding(false);
        if (!this.level.isClientSide && isOwner(player)) {
            player.yRot = this.yRot;
            player.xRot = this.xRot;
            player.startRiding(this);
        }

    }

    protected boolean isImmobile() {
        return super.isImmobile() && this.isVehicle() && this.isSaddled() || this.isStanding();
    }

    public void tick() {
        super.tick();

        // fix the wierd player sized hitbox
        this.refreshDimensions();

        // heal over time
        if (random.nextInt(100) == 0){
            this.heal(1);
        }

        // fall if you dismount while flying
        if (this.isNoGravity() && !this.isVehicle()) this.setNoGravity(false);

        if ((this.isControlledByLocalInstance() || this.isEffectiveAi()) && this.standCounter > 0 && ++this.standCounter > 20) {
            this.standCounter = 0;
            this.setStanding(false);
        }

        if (this.tailCounter > 0 && ++this.tailCounter > 8) {
            this.tailCounter = 0;
        }

        if (this.sprintCounter > 0) {
            ++this.sprintCounter;
            if (this.sprintCounter > 300) {
                this.sprintCounter = 0;
            }
        }

        if (this.isStanding()) {
            this.eatAnim = 0.0F;
            
            // this was for shift forwards when you jump
            //this.standAnim += (1.0F - this.standAnim) * 0.4F + 0.05F;
            //if (this.standAnim > 1.0F) {
            //    this.standAnim = 1.0F;
            //}
        } else {
            this.allowStandSliding = false;
            //this.standAnim += (0.8F * this.standAnim * this.standAnim * this.standAnim - this.standAnim) * 0.6F - 0.05F;
            //if (this.standAnim < 0.0F) {
             //   this.standAnim = 0.0F;
            //}
        }

        if (this.getVanillaType() == EntityType.SLIME || this.getVanillaType() == EntityType.MAGMA_CUBE){

        }
        this.squish += (this.targetSquish - this.squish) * 0.5F;
        this.oSquish = this.squish;
        if (this.onGround && !this.wasOnGround) {
            this.targetSquish = -0.5F;
        } else if (!this.onGround && this.wasOnGround) {
            this.targetSquish = 1.0F;
        }

        if (this.justLanded) {
            this.targetSquish = -0.5F;
            this.justLanded = false;
        } else if (this.justAirJumped) {
            this.targetSquish = 1.0F;
            this.justAirJumped = false;
        }

        this.wasOnGround = this.onGround;
        this.decreaseSquish();
    }
    boolean justAirJumped = false;
    boolean justLanded = false;  // unused cause what does landing even mean

    protected void decreaseSquish() {
        this.targetSquish *= 0.6F;
    }


    public void setStanding(boolean p_110219_1_) {
        this.standing = p_110219_1_;
    }

    private void stand() {
        if (this.isControlledByLocalInstance() || this.isEffectiveAi()) {
            this.standCounter = 1;
            this.setStanding(true);
        }

    }

    @Override
    protected boolean isAffectedByFluids() {
        return !this.canBeRiddenInWater(null);
    }

    boolean isFlying = false;
    int slimeHopTimer = 10;
    public void travel(Vector3d travelVec) {
        if (this.isAlive()) {
            if (this.isVehicle() && (this.canFly() || this.canBeRiddenInWater(null)) && !(this.getVanillaType() == EntityType.SLIME)){
                LivingEntity rider = (LivingEntity) this.getPassengers().get(0);

                if (this.canBeRiddenInWater(rider) && this.isInWaterOrBubble()){
                    isFlying = true;
                }
                if (this.canBeRiddenInWater(rider) && !this.isInWaterOrBubble() && this.isFlying && !this.canFly()){
                    isFlying = false;
                }
                this.setOnGround(!isFlying);

                if (isFlying) {
                    this.setNoGravity(true);

                    this.yRot = rider.yRot;
                    this.yRotO = this.yRot;

                    double yComponent = 0;
                    double moveForward = 0;

                    BlockState downState = this.level.getBlockState(this.blockPosition().below(2));
                    boolean downSolid = !(downState.isAir() || (this.canBeRiddenInWater(null) && downState.is(Blocks.WATER)));  // !isAir should be isSolid

                    if (rider.zza > 0) {
                        moveForward = this.getFlyingSpeed();
                        this.xRot = -MathHelper.clamp(rider.xRot, -10, 10);
                        this.setRot(this.yRot, this.xRot);
                        if (rider.xRot < -10 || rider.xRot > 10) {
                            yComponent = -(Math.toRadians(rider.xRot) * this.getFlyingSpeed());
                            if (!isFlying && yComponent > 0) isFlying = true;  // that makes no sense?
                            else if (isFlying && yComponent < 0 && downSolid)
                                isFlying = false;
                        }
                    } else if (rider.zza < 0) {
                        moveForward = -this.getFlyingSpeed();
                        this.xRot = -MathHelper.clamp(rider.xRot, -10, 10);
                        this.setRot(this.yRot, this.xRot);
                        if (rider.xRot < -10 || rider.xRot > 10) {
                            yComponent = (Math.toRadians(rider.xRot) * this.getFlyingSpeed());
                            if (!isFlying && yComponent > 0) isFlying = true;
                            else if (isFlying && yComponent < 0 && downSolid)
                                isFlying = false;
                        }
                    }

                    if (this.isControlledByLocalInstance()){
                        this.flyingSpeed = (float) this.getFlyingSpeed();
                        // this.setSpeed((float) this.getFlyingSpeed());
                        super.travel(new Vector3d(rider.xxa * this.getFlyingSpeed() * 0.5F, yComponent, moveForward));
                    } else if (rider instanceof PlayerEntity) {
                        this.setDeltaMovement(Vector3d.ZERO);
                    }

                    return;
                } else {
                    this.setNoGravity(false);
                }
            }

            if (this.isVehicle() && this.canBeControlledByRider()) {
                LivingEntity livingentity = (LivingEntity)this.getControllingPassenger();
                this.yRot = livingentity.yRot;
                this.yRotO = this.yRot;
                this.xRot = livingentity.xRot * 0.5F;
                this.setRot(this.yRot, this.xRot);
                this.yBodyRot = this.yRot;
                this.yHeadRot = this.yBodyRot;
                float f = livingentity.xxa * 0.5F;
                float f1 = livingentity.zza;
                if (f1 <= 0.0F) {
                    f1 *= 0.25F;
                    this.gallopSoundCounter = 0;
                }

                if (this.onGround && this.playerJumpPendingScale == 0.0F && this.isStanding() && !this.allowStandSliding) {
                    f = 0.0F;
                    f1 = 0.0F;
                }

                boolean allowJump = (this.onGround && !this.isJumping()) || (this.getVanillaType() == EntityType.SLIME && canFly()) ;
                if ((this.playerJumpPendingScale > 0.0F && allowJump)) {
                    this.justAirJumped = true;

                    double d0 = this.getCustomJump() * (double)this.playerJumpPendingScale * (double)this.getBlockJumpFactor();
                    double d1;
                    if (this.hasEffect(Effects.JUMP)) {
                        d1 = d0 + (double)((float)(this.getEffect(Effects.JUMP).getAmplifier() + 1) * 0.1F);
                    } else {
                        d1 = d0;
                    }
                    if (this.getVanillaType() == EntityType.SLIME) d1 *= 1.75;
                    if (this.canFly() && !(this.getVanillaType() == EntityType.SLIME)) d1 = 0.5;

                    Vector3d vector3d = this.getDeltaMovement();
                    this.setDeltaMovement(vector3d.x, d1, vector3d.z);
                    this.setIsJumping(true);
                    this.hasImpulse = true;
                    ForgeHooks.onLivingJump(this);
                    if (f1 > 0.0F) {
                        float f2 = MathHelper.sin(this.yRot * ((float)Math.PI / 180F));
                        float f3 = MathHelper.cos(this.yRot * ((float)Math.PI / 180F));
                        float force = this.getVanillaType() == EntityType.SLIME && canFly() ? 2 : 1;
                        this.setDeltaMovement(this.getDeltaMovement().add((double)(-0.4F * f2 * this.playerJumpPendingScale * force), 0.0D, (double)(0.4F * f3 * this.playerJumpPendingScale * force)));
                    }

                    this.playerJumpPendingScale = 0.0F;
                    if (canFly()) isFlying = true;
                } else if (this.onGround && this.getVanillaType() == EntityType.SLIME && (f != 0 || f1 != 0)){
                    if (slimeHopTimer >= 3){
                        doSlimeJump((float) f1, 1, 1);
                        slimeHopTimer = 0;
                    }
                    slimeHopTimer++;
                }

                this.flyingSpeed = this.getSpeed() * 0.1F;
                if (this.isControlledByLocalInstance()) {
                    float speed = this.isSlowOnLand() ? 0.05F : this.getWalkingSpeed();
                    this.setSpeed(speed);
                    super.travel(new Vector3d((double)f, travelVec.y, (double)f1));
                } else if (livingentity instanceof PlayerEntity) {
                    this.setDeltaMovement(Vector3d.ZERO);
                }

                if (this.onGround) {
                    this.playerJumpPendingScale = 0.0F;
                    this.setIsJumping(false);
                }

                this.calculateEntityAnimation(this, false);

                // flight logic
                if (livingentity.xRot < -25 && livingentity.zza > 0) isFlying = true;
            } else {
                float speed = this.isSlowOnLand() ? 0.05F : this.getWalkingSpeed();
                this.setSpeed(speed);
                this.flyingSpeed = 0.02F;
                if (this.onGround && this.getVanillaType() == EntityType.SLIME && (travelVec.x != 0 || travelVec.z != 0)){
                    if (slimeHopTimer >= 5){
                        doSlimeJump((float) travelVec.z, 0.5,0.2F);
                        slimeHopTimer = 0;
                    }
                    slimeHopTimer++;
                }
                super.travel(travelVec);
            }


        }
    }

    private float getWalkingSpeed() {
        float speed = (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) + (this.entityData.get(SPEED_CORES) * MountsConfig.getWalkingSpeedPerCore());
        return Math.max(speed, 0.025F);
    }

    private float getFlyingSpeed() {
        float speed = 0.25F + (this.entityData.get(SPEED_CORES) * MountsConfig.getFlyingSpeedPerCore());
        return Math.max(speed, 0.025F);
    }

    private void doSlimeJump(float f1, double yScale, double force) {
        double d1 = 0.8D * yScale;
        Vector3d vector3d = this.getDeltaMovement();
        this.setDeltaMovement(vector3d.x, d1, vector3d.z);
        this.setIsJumping(true);
        this.hasImpulse = true;
        if (f1 > 0.0F) {
            float f2 = MathHelper.sin(this.yRot * ((float)Math.PI / 180F));
            float f3 = MathHelper.cos(this.yRot * ((float)Math.PI / 180F));
            this.setDeltaMovement(this.getDeltaMovement().add((double)(-0.4F * f2 * force), 0.0D, (double)(0.4F * f3 * force)));
        }
        // this.onGround = false;  // without this it jitters maybe use 10 tick timer instead
    }

    protected void playJumpSound() {
        if (this.getVanillaType() == EntityType.SLIME || this.getVanillaType() == EntityType.MAGMA_CUBE){
            this.playSound(SoundEvents.SLIME_JUMP, 0.4F, 1.0F);
        }
    }

    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    @OnlyIn(Dist.CLIENT)
    public void onPlayerJump(int p_110206_1_) {
        if (p_110206_1_ < 0) {
            p_110206_1_ = 0;
        } else {
            this.allowStandSliding = true;
            this.stand();
        }

        if (p_110206_1_ >= 90) {
            this.playerJumpPendingScale = 1.0F;
        } else {
            this.playerJumpPendingScale = 0.4F + 0.4F * (float)p_110206_1_ / 90.0F;
        }
    }

    public boolean canJump() {
        return this.isSaddled();
    }

    public void handleStartJump(int p_184775_1_) {
        this.allowStandSliding = true;
        this.stand();
        this.playJumpSound();
    }

    public void handleStopJump() {
    }


    public void positionRider(Entity p_184232_1_) {
        if (this.hasPassenger(p_184232_1_)) {
            float xDir = MathHelper.sin(this.yBodyRot * ((float)Math.PI / 180F));
            float zDir = MathHelper.cos(this.yBodyRot * ((float)Math.PI / 180F));
            float shiftX = 0;
            float shiftZ = 0;

            double d0 = this.getY() + this.getPassengersRidingOffset() + p_184232_1_.getMyRidingOffset();

            if (this.getVanillaType() == EntityType.ZOMBIE || this.getVanillaType() == EntityType.SKELETON || this.getVanillaType() == EntityType.CREEPER || this.getVanillaType() == EntityType.SNOW_GOLEM){
                shiftX = xDir * 0.45F;
                shiftZ = zDir * -0.45F;
                d0 -= 0.25D;
            }

            if (this.isBaby() && (this.getVanillaType() == EntityType.CHICKEN || this.getVanillaType() == EntityType.WITHER || this.getVanillaType() == EntityType.COW)){
                shiftX = xDir * 0.45F;
                shiftZ = zDir * -0.45F;
            }

            if (!this.isBaby() && (this.getVanillaType() == EntityType.FOX)){
                shiftX = xDir * 0.45F;
                shiftZ = zDir * -0.45F;
            }

            if (!this.isBaby() && this.getVanillaType() == EntityType.FOX){
                d0 -= 0.1F;
            }

            if (!this.isBaby() && this.getVanillaType() == EntityType.SLIME){
                d0 -= 0.16F;
            }

            if (!this.isBaby() && this.getVanillaType() == EntityType.RAVAGER){
                d0 -= 0.2F;
            }

            if (!this.isBaby() && this.getVanillaType() == EntityType.WITHER){
                d0 -= 0.3F;
            }

            if (this.isBaby() && (this.getVanillaType() == EntityType.CAT || this.getVanillaType() == EntityType.LLAMA)){
                d0 -= 0.1F;
            }

            if (this.isBaby() && this.getVanillaType() == EntityType.TURTLE){
                d0 -= 0.25F;
            }

            p_184232_1_.setPos(this.getX() + shiftX, d0, this.getZ() + shiftZ);
        }
        if (p_184232_1_ instanceof MobEntity) {
            MobEntity mobentity = (MobEntity)p_184232_1_;
            this.yBodyRot = mobentity.yBodyRot;
        }

        /*  // shift forwards when you jump
        if (this.standAnimO > 0.0F) {
            float f3 = MathHelper.sin(this.yBodyRot * ((float)Math.PI / 180F));
            float f = MathHelper.cos(this.yBodyRot * ((float)Math.PI / 180F));
            float f1 = 0.7F * this.standAnimO;
            float f2 = 0.15F * this.standAnimO;
            p_184232_1_.setPos(this.getX() + (double)(f1 * f3), this.getY() + this.getPassengersRidingOffset() + p_184232_1_.getMyRidingOffset() + (double)f2, this.getZ() - (double)(f1 * f));
            if (p_184232_1_ instanceof LivingEntity) {
                ((LivingEntity)p_184232_1_).yBodyRot = this.yBodyRot;
            }
        }

         */

    }

    @Override
    public double getPassengersRidingOffset() {
        double scale = 0.75D;
        if (!this.isBaby() && (this.getVanillaType() == EntityType.SLIME || this.getVanillaType() == EntityType.GHAST || this.getVanillaType() == EntityType.WITHER || this.getVanillaType() == EntityType.RAVAGER || this.getVanillaType() == EntityType.HOGLIN)) scale = 1;
        if (this.isBaby()) scale = 0.6;
        return (double)this.getDimensions(getPose()).height * scale;
    }

    protected float generateRandomMaxHealth() {
        return 15.0F + (float)this.random.nextInt(8) + (float)this.random.nextInt(9);
    }

    protected double generateRandomJumpStrength() {
        return (double)0.4F + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D;
    }

    protected double generateRandomSpeed() {
        return ((double)0.45F + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D) * 0.25D;
    }

    public boolean onClimbable() {
        return false;
    }

    public float getScale() {
        boolean smolBaby = this.getVanillaType() == EntityType.GHAST || this.getVanillaType() == EntityType.SLIME;
        return this.isBaby() ? (smolBaby ? 0.2F : 0.45F) : 1.0F;
    }

    protected float getStandingEyeHeight(Pose p_213348_1_, EntitySize p_213348_2_) {
        return p_213348_2_.height * 0.95F;
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Nullable
    private Vector3d getDismountLocationInDirection(Vector3d p_234236_1_, LivingEntity p_234236_2_) {
        double d0 = this.getX() + p_234236_1_.x;
        double d1 = this.getBoundingBox().minY;
        double d2 = this.getZ() + p_234236_1_.z;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(Pose pose : p_234236_2_.getDismountPoses()) {
            blockpos$mutable.set(d0, d1, d2);
            double d3 = this.getBoundingBox().maxY + 0.75D;

            while(true) {
                double d4 = this.level.getBlockFloorHeight(blockpos$mutable);
                if ((double)blockpos$mutable.getY() + d4 > d3) {
                    break;
                }

                if (TransportationHelper.isBlockFloorValid(d4)) {
                    AxisAlignedBB axisalignedbb = p_234236_2_.getLocalBoundsForPose(pose);
                    Vector3d vector3d = new Vector3d(d0, (double)blockpos$mutable.getY() + d4, d2);
                    if (TransportationHelper.canDismountTo(this.level, p_234236_2_, axisalignedbb.move(vector3d))) {
                        p_234236_2_.setPose(pose);
                        return vector3d;
                    }
                }

                blockpos$mutable.move(Direction.UP);
                if (!((double)blockpos$mutable.getY() < d3)) {
                    break;
                }
            }
        }

        return null;
    }

    public Vector3d getDismountLocationForPassenger(LivingEntity p_230268_1_) {
        Vector3d vector3d = getCollisionHorizontalEscapeVector((double)this.getBbWidth(), (double)p_230268_1_.getBbWidth(), this.yRot + (p_230268_1_.getMainArm() == HandSide.RIGHT ? 90.0F : -90.0F));
        Vector3d vector3d1 = this.getDismountLocationInDirection(vector3d, p_230268_1_);
        if (vector3d1 != null) {
            return vector3d1;
        } else {
            Vector3d vector3d2 = getCollisionHorizontalEscapeVector((double)this.getBbWidth(), (double)p_230268_1_.getBbWidth(), this.yRot + (p_230268_1_.getMainArm() == HandSide.LEFT ? 90.0F : -90.0F));
            Vector3d vector3d3 = this.getDismountLocationInDirection(vector3d2, p_230268_1_);
            return vector3d3 != null ? vector3d3 : this.position();
        }
    }
}
