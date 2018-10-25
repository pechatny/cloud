package ru.pechatny.cloud.client;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;

public class TreeContextMenu extends ContextMenu {
    private TreeItem treeItem;

    public TreeContextMenu(TreeItem treeItem, MenuItem... items) {
        super(items);
        this.treeItem = treeItem;
    }

    public TreeItem getTreeItem() {
        return treeItem;
    }
}
