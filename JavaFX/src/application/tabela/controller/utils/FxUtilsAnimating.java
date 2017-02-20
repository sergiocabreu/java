package application.controller.utils;

import java.util.function.Supplier;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class FxUtilsAnimating {

	public static void fadeInAnimating(final Node node, final Pane parent) {
		FxUtilsAnimating.addAnimating(node, parent, 
				() -> {
					final FadeTransition transition = new FadeTransition(Duration.millis(250), node);
					transition.setFromValue(0);
				    transition.setToValue(1);
				    transition.setInterpolator(Interpolator.EASE_IN);
					return transition;
				}
		);
	}

	public static void fadeOutAnimating(final Node node, final Pane parent) {
		FxUtilsAnimating.removeAnimating(node, parent, 
				() -> {
					final FadeTransition transition = new FadeTransition(Duration.millis(250), node);
					transition.setFromValue(node.getOpacity());
				    transition.setToValue(0);
				    transition.setInterpolator(Interpolator.EASE_BOTH);
					return transition;
				}
		);
	}
	
	public static void addAnimating(final Node node, final Pane parent, final Supplier<Animation> animationCreator) {
		if (!parent.getChildren().contains(node)) {
			parent.getChildren().add(node);
			animationCreator.get().play();
		}
	}
	
	public static void removeAnimating(final Node node, final Pane parent, final Supplier<Animation> animationCreator) {
		if (parent.getChildren().contains(node)) {
			final Animation animation = animationCreator.get();
			animation.setOnFinished(finishHim -> {
				parent.getChildren().remove(node);
			});
			animation.play();
		}
	}

}
