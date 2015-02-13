namespace GauchoGrub.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class navigation : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.MenuItems", "Menu_Id", c => c.Int());
            CreateIndex("dbo.MenuItems", "MenuCategoryId");
            CreateIndex("dbo.MenuItems", "MenuItemTypeId");
            CreateIndex("dbo.MenuItems", "Menu_Id");
            CreateIndex("dbo.Menus", "EventId");
            CreateIndex("dbo.RepeatedEvents", "DiningCommonId");
            CreateIndex("dbo.RepeatedEvents", "MealId");
            AddForeignKey("dbo.MenuItems", "MenuCategoryId", "dbo.MenuCategories", "Id", cascadeDelete: true);
            AddForeignKey("dbo.MenuItems", "MenuItemTypeId", "dbo.MenuItemTypes", "Id", cascadeDelete: true);
            AddForeignKey("dbo.RepeatedEvents", "DiningCommonId", "dbo.DiningCommons", "Id", cascadeDelete: true);
            AddForeignKey("dbo.RepeatedEvents", "MealId", "dbo.Meals", "Id", cascadeDelete: true);
            AddForeignKey("dbo.Menus", "EventId", "dbo.RepeatedEvents", "Id", cascadeDelete: true);
            AddForeignKey("dbo.MenuItems", "Menu_Id", "dbo.Menus", "Id");
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.MenuItems", "Menu_Id", "dbo.Menus");
            DropForeignKey("dbo.Menus", "EventId", "dbo.RepeatedEvents");
            DropForeignKey("dbo.RepeatedEvents", "MealId", "dbo.Meals");
            DropForeignKey("dbo.RepeatedEvents", "DiningCommonId", "dbo.DiningCommons");
            DropForeignKey("dbo.MenuItems", "MenuItemTypeId", "dbo.MenuItemTypes");
            DropForeignKey("dbo.MenuItems", "MenuCategoryId", "dbo.MenuCategories");
            DropIndex("dbo.RepeatedEvents", new[] { "MealId" });
            DropIndex("dbo.RepeatedEvents", new[] { "DiningCommonId" });
            DropIndex("dbo.Menus", new[] { "EventId" });
            DropIndex("dbo.MenuItems", new[] { "Menu_Id" });
            DropIndex("dbo.MenuItems", new[] { "MenuItemTypeId" });
            DropIndex("dbo.MenuItems", new[] { "MenuCategoryId" });
            DropColumn("dbo.MenuItems", "Menu_Id");
        }
    }
}
