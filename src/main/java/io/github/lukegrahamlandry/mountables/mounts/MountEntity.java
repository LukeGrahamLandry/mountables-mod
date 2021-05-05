package io.github.lukegrahamlandry.mountables.mounts;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.init.ItemInit;
import io.github.lukegrahamlandry.mountables.items.MountSummonItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
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
    public static final int maxHealth = 7;

    private ItemStack summonStack;
    private EntityType vanillaType;

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
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource p_213333_1_, int p_213333_2_, boolean p_213333_3_) {
        super.dropCustomDeathLoot(p_213333_1_, p_213333_2_, p_213333_3_);
        MountSummonItem.writeNBT(this.summonStack, this.vanillaType, this.getTextureType(), 1, this.canFly(), this.isBaby());
        this.spawnAtLocation(summonStack);
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
            boolean textureSuccess = false;
            if (this.vanillaType == EntityType.SHEEP) textureSuccess = updateSheepTexture(itemstack.getItem());
            if (this.vanillaType == EntityType.COW) textureSuccess = updateCowTexture(itemstack.getItem());
            if (this.vanillaType == EntityType.PIG) textureSuccess = updatePigTexture(itemstack.getItem());
            if (this.vanillaType == EntityType.SNOW_GOLEM) textureSuccess = updateSnowmanTexture(itemstack.getItem());
            if (this.vanillaType == EntityType.SPIDER) textureSuccess = updateSpiderTexture(itemstack.getItem());
            if (this.vanillaType == EntityType.CAT && itemstack.getItem() == Items.COD){
                this.setTextureType((this.getTextureType() + 1) % (CatEntity.TEXTURE_BY_TYPE.size() + 1));
                textureSuccess = true;
            }
            if (this.vanillaType == EntityType.CAT && itemstack.getItem() == Items.WHEAT){
                this.setTextureType((this.getTextureType() + 1) % 4);
                textureSuccess = true;
            }
            if (this.vanillaType == EntityType.FOX) textureSuccess = updateFoxTexture(itemstack.getItem());
            if (this.vanillaType == EntityType.LLAMA && itemstack.getItem() == Items.WHEAT){
                this.setTextureType((this.getTextureType() + 1) % 4);
                textureSuccess = true;
            }
            if (this.vanillaType == EntityType.PANDA && itemstack.getItem() == Items.BAMBOO){
                this.setTextureType((this.getTextureType() + 1) % 7);
                textureSuccess = true;
            }
            if (this.vanillaType == EntityType.ZOMBIE) textureSuccess = updateZombieTexture(itemstack.getItem());
            if (this.vanillaType == EntityType.SKELETON) textureSuccess = updateSkeletonTexture(itemstack.getItem());
            if (this.vanillaType == EntityType.HOGLIN) textureSuccess = updateHogTexture(itemstack.getItem());
            if (this.vanillaType == EntityType.SQUID) textureSuccess = updateSquidTexture(itemstack.getItem());
            if (this.vanillaType == EntityType.GUARDIAN) textureSuccess = updateGuardianTexture(itemstack.getItem());
            if (itemstack.getItem() == Items.FEATHER) {
                textureSuccess = true;
                this.entityData.set(CAN_FLY, true);
            }
            if (textureSuccess){
                if (itemstack.getItem() == Items.WATER_BUCKET) player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                else if (itemstack.getItem() != Items.SHEARS) itemstack.shrink(1);
                if (!level.isClientSide()) doParticles(ParticleTypes.HAPPY_VILLAGER);
                return ActionResultType.sidedSuccess(this.level.isClientSide);
            }

            if (itemstack.getItem() == Items.MILK_BUCKET && allowBaby()){
                this.setChild(!this.isBaby());
                if (!level.isClientSide()) doParticles(ParticleTypes.HAPPY_VILLAGER);
                player.setItemInHand(hand, new ItemStack(Items.BUCKET));
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
                // crashes on client if leaded from nbt because stack is null
                MountSummonItem.writeNBT(this.summonStack, this.vanillaType, this.getTextureType(), (int) this.getHealth(), this.canFly(), this.isBaby());
                player.setItemInHand(hand, this.summonStack);
            }
            this.remove();
            return ActionResultType.SUCCESS;
        }

        this.doPlayerRide(player);
        return ActionResultType.sidedSuccess(this.level.isClientSide);
    }

    private boolean allowBaby() {
        return this.vanillaType == EntityType.COW || this.vanillaType == EntityType.PIG || this.vanillaType == EntityType.SHEEP
                || this.vanillaType == EntityType.WOLF || this.vanillaType == EntityType.FOX || this.vanillaType == EntityType.CAT
                || this.vanillaType == EntityType.LLAMA || this.vanillaType == EntityType.PANDA || this.vanillaType == EntityType.ZOMBIE
                || this.vanillaType == EntityType.WITHER || this.vanillaType == EntityType.GHAST || this.vanillaType == EntityType.HOGLIN
                || this.vanillaType == EntityType.TURTLE || this.vanillaType == EntityType.CHICKEN;
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
        this.entityData.define(VANILLA_TYPE, "");
        this.entityData.define(CAN_FLY, false);
        this.entityData.define(IS_BABY, false);
        this.entityData.define(OWNER, Optional.empty());
    }

    public void setTextureType(int x){
        this.entityData.set(TEXTURE, x);
    }

    public int getTextureType(){
        return this.entityData.get(TEXTURE);
    }

    public EntityType getVanillaType() {
        return EntityType.byString(this.entityData.get(VANILLA_TYPE)).orElse(EntityType.CREEPER);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        this.summonStack.getTag().putInt("health", (int) this.getHealth());
        this.summonStack.getTag().putInt("texturetype", this.getTextureType());
        MountSummonItem.writeNBT(this.summonStack, this.vanillaType, this.getTextureType(), (int) this.getHealth(), this.canFly(), this.isBaby());
        nbt.put("summon", this.summonStack.getTag());
        nbt.putUUID("owner", getOwnerUUID());
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
        return this.entityData.get(CAN_FLY);
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
        return this.getVanillaType() == EntityType.SQUID || this.getVanillaType() == EntityType.TURTLE || this.getVanillaType() == EntityType.GUARDIAN;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return this.canBeRiddenInWater(null);
    }

    // *** TEXTURE TYPES BY ENTITY *** //

    private static final Map<Item, DyeColor> woolBlocks = new HashMap<>();
    static {
        woolBlocks.put(Items.WHITE_WOOL, DyeColor.WHITE);
        woolBlocks.put(Items.ORANGE_WOOL, DyeColor.ORANGE);
        woolBlocks.put(Items.MAGENTA_WOOL, DyeColor.MAGENTA);
        woolBlocks.put(Items.LIGHT_BLUE_WOOL, DyeColor.LIGHT_BLUE);
        woolBlocks.put(Items.YELLOW_WOOL, DyeColor.YELLOW);
        woolBlocks.put(Items.LIME_WOOL, DyeColor.LIME);
        woolBlocks.put(Items.PINK_WOOL, DyeColor.PINK);
        woolBlocks.put(Items.GRAY_WOOL, DyeColor.GRAY);
        woolBlocks.put(Items.LIGHT_GRAY_WOOL, DyeColor.LIGHT_GRAY);
        woolBlocks.put(Items.CYAN_WOOL, DyeColor.CYAN);
        woolBlocks.put(Items.PURPLE_WOOL, DyeColor.PURPLE);
        woolBlocks.put(Items.BLUE_WOOL, DyeColor.BLUE);
        woolBlocks.put(Items.BROWN_WOOL, DyeColor.BROWN);
        woolBlocks.put(Items.GREEN_WOOL, DyeColor.GREEN);
        woolBlocks.put(Items.RED_WOOL, DyeColor.RED);
        woolBlocks.put(Items.BLACK_WOOL, DyeColor.BLACK);
    }

    private boolean updateSheepTexture(Item item){
        if (item instanceof DyeItem){
            this.setTextureType(((DyeItem)item).getDyeColor().getId());
        } else if (item == Items.SHEARS){
            this.setTextureType(17);
        } else if (item == Items.NETHER_STAR){
            this.setTextureType(16);
        } else if (woolBlocks.containsKey(item)) {
            this.setTextureType(woolBlocks.get(item).getId());
        } else {
            return false;
        }
        return true;
    }

    private boolean updateCowTexture(Item item){
        if (item == Items.RED_MUSHROOM){
            this.setTextureType(1);
        } else if (item == Items.BROWN_MUSHROOM){
            this.setTextureType(2);
        } else if (item == Items.SHEARS){
            this.setTextureType(0);
        } else {
            return false;
        }
        return true;
    }

    private boolean updatePigTexture(Item item){
        if (item == Items.GLOWSTONE_DUST){
            this.setTextureType(1);
        } else if (item == Items.CARROT){
            this.setTextureType(0);
        } else {
            return false;
        }
        return true;
    }

    private boolean updateSnowmanTexture(Item item){
        if (item == Items.CARVED_PUMPKIN){
            this.setTextureType(0);
        } else if (item == Items.SHEARS){
            this.setTextureType(1);
        } else {
            return false;
        }
        return true;
    }

    private boolean updateSpiderTexture(Item item){
        if (item == Items.STRING){
            this.setTextureType(0);
        } else if (item == Items.SPIDER_EYE){
            this.setTextureType(1);
        } else {
            return false;
        }
        return true;
    }

    private boolean updateFoxTexture(Item item){
        if (item == Items.SPRUCE_LOG){
            this.setTextureType(0);
        } else if (item == Items.SNOWBALL){
            this.setTextureType(1);
        } else {
            return false;
        }
        return true;
    }

    private boolean updateZombieTexture(Item item){
        if (item == Items.ROTTEN_FLESH){
            this.setTextureType(0);
        } else if (item == Items.SAND){
            this.setTextureType(1);
        } else if (item == Items.WATER_BUCKET){
            this.setTextureType(2);
        } else {
            return false;
        }
        return true;
    }

    private boolean updateSkeletonTexture(Item item){
        if (item == Items.BONE){
            this.setTextureType(0);
        } else if (item == Items.COAL){
            this.setTextureType(1);
        } else if (item == Items.SNOWBALL){
            this.setTextureType(2);
        } else {
            return false;
        }
        return true;
    }

    private boolean updateHogTexture(Item item){
        if (item == Items.PORKCHOP){
            this.setTextureType(0);
        } else if (item == Items.ROTTEN_FLESH){
            this.setTextureType(1);
        } else {
            return false;
        }
        return true;
    }

    private boolean updateSquidTexture(Item item){
        if (item == Items.INK_SAC){
            this.setTextureType(0);
        } else if (item == Items.GLOWSTONE){
            this.setTextureType(1);
        } else {
            return false;
        }
        return true;
    }

    private boolean updateGuardianTexture(Item item){
        if (item == Items.PRISMARINE_SHARD){
            this.setTextureType(0);
        } else if (item == Items.COOKED_COD){
            this.setTextureType(1);
        } else {
            return false;
        }
        return true;
    }

    // *** HORSE *** //

    public MountEntity(EntityType<? extends CreatureEntity> p_i48567_1_, World p_i48567_2_) {
        super(p_i48567_1_, p_i48567_2_);
        this.maxUpStep = 1;
    }

    private int standCounter;
    public int tailCounter;
    public int sprintCounter;
    protected boolean isJumping;
    protected Inventory inventory;
    protected float playerJumpPendingScale;
    private boolean allowStandSliding;
    private float eatAnim;
    private float eatAnimO;
    private float standAnim;
    private float standAnimO;
    protected boolean canGallop = true;
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
            this.playSound(SoundEvents.HORSE_LAND, 0.4F, 1.0F);
        }

        int i = this.calculateFallDamage(p_225503_1_, p_225503_2_);
        if (i <= 0 || this.canFly()) {
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
        return 0.85D;// this.getAttributeValue(Attributes.JUMP_STRENGTH);
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

        return null;
    }

    @Nullable
    protected SoundEvent getAngrySound() {
        this.stand();
        return null;
    }

    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        if (!p_180429_2_.getMaterial().isLiquid()) {
            BlockState blockstate = this.level.getBlockState(p_180429_1_.above());
            SoundType soundtype = p_180429_2_.getSoundType(level, p_180429_1_, this);
            if (blockstate.is(Blocks.SNOW)) {
                soundtype = blockstate.getSoundType(level, p_180429_1_, this);
            }

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

        }
    }

    protected void playGallopSound(SoundType p_190680_1_) {
        this.playSound(SoundEvents.HORSE_GALLOP, p_190680_1_.getVolume() * 0.15F, p_190680_1_.getPitch());
    }

    public int getMaxSpawnClusterSize() {
        return 6;
    }

    public int getMaxTemper() {
        return 100;
    }

    protected float getSoundVolume() {
        return 0.8F;
    }

    public int getAmbientSoundInterval() {
        return 400;
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

    private void moveTail() {
        this.tailCounter = 1;
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (this.inventory != null) {
            for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);
                if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
                    this.spawnAtLocation(itemstack);
                }
            }

        }
    }


    public void tick() {
        super.tick();

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

        this.standAnimO = this.standAnim;
        if (this.isStanding()) {
            this.eatAnim = 0.0F;
            this.eatAnimO = this.eatAnim;
            
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
    final double flightSpeed = 0.25D;
    public void travel(Vector3d travelVec) {
        if (this.isAlive()) {
            if (this.isVehicle() && (this.canFly() || this.canBeRiddenInWater(null))){
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
                        moveForward = flightSpeed;
                        this.xRot = -MathHelper.clamp(rider.xRot, -10, 10);
                        this.setRot(this.yRot, this.xRot);
                        if (rider.xRot < -10 || rider.xRot > 10) {
                            yComponent = -(Math.toRadians(rider.xRot) * flightSpeed);
                            if (!isFlying && yComponent > 0) isFlying = true;  // that makes no sense?
                            else if (isFlying && yComponent < 0 && downSolid)
                                isFlying = false;
                        }
                    } else if (rider.zza < 0) {
                        moveForward = -flightSpeed;
                        this.xRot = -MathHelper.clamp(rider.xRot, -10, 10);
                        this.setRot(this.yRot, this.xRot);
                        if (rider.xRot < -10 || rider.xRot > 10) {
                            yComponent = (Math.toRadians(rider.xRot) * flightSpeed);
                            if (!isFlying && yComponent > 0) isFlying = true;
                            else if (isFlying && yComponent < 0 && downSolid)
                                isFlying = false;
                        }
                    }

                    if (this.isControlledByLocalInstance()){
                        this.flyingSpeed = (float) flightSpeed;
                        // this.setSpeed((float) flightSpeed);
                        super.travel(new Vector3d(rider.xxa, yComponent, moveForward));
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

                if (this.playerJumpPendingScale > 0.0F && !this.isJumping() && this.onGround) {
                    double d0 = this.getCustomJump() * (double)this.playerJumpPendingScale * (double)this.getBlockJumpFactor();
                    double d1;
                    if (this.hasEffect(Effects.JUMP)) {
                        d1 = d0 + (double)((float)(this.getEffect(Effects.JUMP).getAmplifier() + 1) * 0.1F);
                    } else {
                        d1 = d0;
                    }
                    if (this.canFly()) d1 = 0.3;

                    Vector3d vector3d = this.getDeltaMovement();
                    this.setDeltaMovement(vector3d.x, d1, vector3d.z);
                    this.setIsJumping(true);
                    this.hasImpulse = true;
                    ForgeHooks.onLivingJump(this);
                    if (f1 > 0.0F) {
                        float f2 = MathHelper.sin(this.yRot * ((float)Math.PI / 180F));
                        float f3 = MathHelper.cos(this.yRot * ((float)Math.PI / 180F));
                        this.setDeltaMovement(this.getDeltaMovement().add((double)(-0.4F * f2 * this.playerJumpPendingScale), 0.0D, (double)(0.4F * f3 * this.playerJumpPendingScale)));
                    }

                    this.playerJumpPendingScale = 0.0F;
                    if (canFly()) isFlying = true;
                }

                this.flyingSpeed = this.getSpeed() * 0.1F;
                if (this.isControlledByLocalInstance()) {
                    float speed = this.canBeRiddenInWater(null) ? 0.05F : (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
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
                float speed = this.canBeRiddenInWater(null) ? 0.05F : (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
                this.setSpeed(speed);
                this.flyingSpeed = 0.02F;
                super.travel(travelVec);
            }


        }
    }

    protected void playJumpSound() {
        this.playSound(SoundEvents.HORSE_JUMP, 0.4F, 1.0F);
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
        super.positionRider(p_184232_1_);
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
        if (!this.isBaby() && (this.getVanillaType() == EntityType.GHAST || this.getVanillaType() == EntityType.WITHER || this.getVanillaType() == EntityType.RAVAGER || this.getVanillaType() == EntityType.HOGLIN)) scale = 1;
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
        return this.isBaby() ? (this.getVanillaType() == EntityType.GHAST ? 0.25F : 0.5F) : 1.0F;
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
