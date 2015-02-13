namespace GauchoGrub.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class unique_constraints : DbMigration
    {
        public override void Up()
        {
            AlterColumn("dbo.DiningCommons", "Name", c => c.String(nullable: false, maxLength: 450));
            AlterColumn("dbo.Meals", "Name", c => c.String(nullable: false, maxLength: 450));
            AlterColumn("dbo.MenuCategories", "Name", c => c.String(nullable: false, maxLength: 450));
            AlterColumn("dbo.MenuItems", "Title", c => c.String(nullable: false, maxLength: 450));
            AlterColumn("dbo.MenuItemTypes", "Name", c => c.String(nullable: false, maxLength: 450));
            CreateIndex("dbo.DiningCommons", "Name", unique: true);
            CreateIndex("dbo.Meals", "Name", unique: true);
            CreateIndex("dbo.MenuCategories", "Name", unique: true);
            CreateIndex("dbo.MenuItems", "Title", unique: true);
            CreateIndex("dbo.MenuItemTypes", "Name", unique: true);
            DropColumn("dbo.DiningCommons", "Latitude");
            DropColumn("dbo.DiningCommons", "Longitude");
        }
        
        public override void Down()
        {
            AddColumn("dbo.DiningCommons", "Longitude", c => c.Double(nullable: false));
            AddColumn("dbo.DiningCommons", "Latitude", c => c.Double(nullable: false));
            DropIndex("dbo.MenuItemTypes", new[] { "Name" });
            DropIndex("dbo.MenuItems", new[] { "Title" });
            DropIndex("dbo.MenuCategories", new[] { "Name" });
            DropIndex("dbo.Meals", new[] { "Name" });
            DropIndex("dbo.DiningCommons", new[] { "Name" });
            AlterColumn("dbo.MenuItemTypes", "Name", c => c.String(nullable: false));
            AlterColumn("dbo.MenuItems", "Title", c => c.String(nullable: false));
            AlterColumn("dbo.MenuCategories", "Name", c => c.String(nullable: false));
            AlterColumn("dbo.Meals", "Name", c => c.String(nullable: false));
            AlterColumn("dbo.DiningCommons", "Name", c => c.String(nullable: false));
        }
    }
}
