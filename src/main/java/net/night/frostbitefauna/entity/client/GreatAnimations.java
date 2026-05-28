package net.night.frostbitefauna.entity.client;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class GreatAnimations {
    public static final Animation ANIM_GREAT_WALK = Animation.Builder.create(3f).looping()
            .addBoneAnimation("Body",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-2.5f, 0f, -2.5f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.08343333f, AnimationHelper.createRotationalVector(-2.5f, 0f, -2.45f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.75f, AnimationHelper.createRotationalVector(0f, 0f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.5f, AnimationHelper.createRotationalVector(-2.5f, 0f, 2.5f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.5834333f, AnimationHelper.createRotationalVector(-2.5f, 0f, 2.45f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(2.25f, AnimationHelper.createRotationalVector(0f, 0f, -1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(3f, AnimationHelper.createRotationalVector(-2.5f, 0f, -2.5f),
                                    Transformation.Interpolations.LINEAR))).build();
    public static final Animation ANIM_GREAT_IDLE = Animation.Builder.create(3f).looping()
            .addBoneAnimation("Nose",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 2.5f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.75f, AnimationHelper.createRotationalVector(0f, 0f, -2.5f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.5f, AnimationHelper.createRotationalVector(0f, 0f, 2.5f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(2.25f, AnimationHelper.createRotationalVector(0f, 0f, -2.5f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(3f, AnimationHelper.createRotationalVector(0f, 0f, 2.5f),
                                    Transformation.Interpolations.LINEAR))).build();
}
