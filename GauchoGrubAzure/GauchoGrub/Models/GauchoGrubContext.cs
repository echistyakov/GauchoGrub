﻿using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;

namespace GauchoGrub.Models
{
    public class GauchoGrubContext : DbContext
    {
        // You can add custom code to this file. Changes will not be overwritten.
        // 
        // If you want Entity Framework to drop and regenerate your database
        // automatically whenever you change your model schema, please use data migrations.
        // For more information refer to the documentation:
        // http://msdn.microsoft.com/en-us/data/jj591621.aspx
    
        public GauchoGrubContext() : base("name=GauchoGrubContext")
        {
        }

        public System.Data.Entity.DbSet<GauchoGrub.Models.DiningCommon> DiningCommons { get; set; }

        public System.Data.Entity.DbSet<GauchoGrub.Models.MenuItem> MenuItems { get; set; }

        public System.Data.Entity.DbSet<GauchoGrub.Models.Menu> Menus { get; set; }

        public System.Data.Entity.DbSet<GauchoGrub.Models.RepeatedEvent> RepeatedEvents { get; set; }
    
    }
}
