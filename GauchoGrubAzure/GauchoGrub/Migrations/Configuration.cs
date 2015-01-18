namespace GauchoGrub.Migrations
{
    using GauchoGrub.Models;
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Migrations;
    using System.Linq;

    internal sealed class Configuration : DbMigrationsConfiguration<GauchoGrub.Models.GauchoGrubContext>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = false;
        }

        protected override void Seed(GauchoGrub.Models.GauchoGrubContext context)
        {
            //  This method will be called after migrating to the latest version.

            context.DiningCommons.AddOrUpdate(
                  x => x.Id,
                  new DiningCommon { Name = "De La Guerra", Latitude = 34.409556, Longitude = -119.845047 },
                  new DiningCommon { Name = "Ortega", Latitude = 34.410963, Longitude = -119.847048 },
                  new DiningCommon { Name = "Carillo", Latitude = 34.410022, Longitude = -119.852730 },
                  new DiningCommon { Name = "Portolla", Latitude = 34.417998, Longitude = -119.868003 }
                );
            context.Meals.AddOrUpdate(
                  x => x.Id,
                  new Meal { Name = "Breakfast" },
                  new Meal { Name = "Brunch" },
                  new Meal { Name = "Lunch" },
                  new Meal { Name = "Dinner" },
                  new Meal { Name = "Late Night" },
                  new Meal { Name = "Sack Meal" }
                );
            context.MenuItemTypes.AddOrUpdate(
                  x => x.Id,
                  new MenuItemType { Name = "Regular" },
                  new MenuItemType { Name = "Vegetarian" },
                  new MenuItemType { Name = "Vegan" }
                );
            context.SaveChanges();
        }
    }
}
