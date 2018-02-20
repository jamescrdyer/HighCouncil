package highcouncil.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import highcouncil.domain.Kingdom;
import highcouncil.domain.Player;
import highcouncil.domain.PlayerTurnResult;
import highcouncil.domain.StatHolder;

@Service
public class CodeResolver {
	enum Category { WEALTH, PIETY, FAVOUR, POPULARITY, HEALTH, MILITARY}
	
	public void resolveCode(String code, StatHolder target) {
		resolveCode(code, target, null);
	}
	public void resolveCode(String code, StatHolder target, PlayerTurnResult playerResult) {
		if (code == null || StringUtils.isBlank(code)) return;
		String[] tokens = code.split("\\s+");
		for (int i = 0; i<tokens.length; i++) {
			String token = tokens[i];
			if (token.compareToIgnoreCase(Category.WEALTH.name()) == 0) {
				int adjustment = getAdjustment(tokens[++i]);
				target.modifyWealth(adjustment);
				if (playerResult != null) {
					playerResult.modifyWealth(adjustment);
				}
			}
			if (token.compareToIgnoreCase(Category.MILITARY.name()) == 0) {
				int adjustment = getAdjustment(tokens[++i]);
				target.modifyMilitary(adjustment);
				if (playerResult != null) {
					playerResult.modifyMilitary(adjustment);
				}
			}
			if (token.compareToIgnoreCase(Category.PIETY.name()) == 0) {
				int adjustment = getAdjustment(tokens[++i]);
				target.modifyPiety(adjustment);
				if (playerResult != null) {
					playerResult.modifyPiety(adjustment);
				}
			}
			if (token.compareToIgnoreCase(Category.POPULARITY.name()) == 0) {
				int adjustment = getAdjustment(tokens[++i]);
				target.modifyPopularity(adjustment);
				if (playerResult != null) {
					playerResult.modifyPopularity(adjustment);
				}
			}
			if (token.compareToIgnoreCase(Category.FAVOUR.name()) == 0) {
				Player player = (Player)target;
				int adjustment = getAdjustment(tokens[++i]);
				player.modifyFavour(adjustment);
				if (playerResult != null) {
					playerResult.modifyFavour(adjustment);
				}
			}
			if (token.compareToIgnoreCase(Category.HEALTH.name()) == 0) {
				Kingdom kingdom = (Kingdom)target;
				kingdom.setHealth(kingdom.getHealth()+getAdjustment(tokens[++i]));
			}
		}
	}
	
	private int getAdjustment(String token) {
		return Integer.parseInt(token);
	}
}
