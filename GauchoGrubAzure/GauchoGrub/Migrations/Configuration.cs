namespace GauchoGrub.Migrations
{
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Migrations;
    using System.Linq;
    using GauchoGrub.Models;

    internal sealed class Configuration : DbMigrationsConfiguration<GauchoGrub.Models.GauchoGrubContext>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = false;
        }

        protected override void Seed(GauchoGrub.Models.GauchoGrubContext context)
        {
            context.DiningCommons.AddOrUpdate(
                  x => x.Id,
                  new DiningCommon { Name = "De La Guerra" },
                  new DiningCommon { Name = "Ortega" },
                  new DiningCommon { Name = "Carillo" },
                  new DiningCommon { Name = "Portolla" }
                );

            //  This method will be called after migrating to the latest version.

            //  You can use the DbSet<T>.AddOrUpdate() helper extension method 
            //  to avoid creating duplicate seed data. E.g.
            //
            //    context.People.AddOrUpdate(
            //      p => p.FullName,
            //      new Person { FullName = "Andrew Peters" },
            //      new Person { FullName = "Brice Lambson" },
            //      new Person { FullName = "Rowan Miller" }
            //    );
            //
        }
    }
}
