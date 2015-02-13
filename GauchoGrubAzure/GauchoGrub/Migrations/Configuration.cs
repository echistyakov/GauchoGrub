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
        }
    }
}
