package nova.core;

public enum Category {
	COMBAT("Combat"), MOVEMENT("Movement"), RENDER("Render"), PLAYER("Player"), CLIENT("Client");
	public final String title;
	Category(String t) { title = t; }
}
