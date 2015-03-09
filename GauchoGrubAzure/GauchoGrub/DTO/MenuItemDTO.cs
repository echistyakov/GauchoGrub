using GauchoGrub.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GauchoGrub.DTO
{
    public class MenuItemDTO
    {
        public int Id { get; set; }

        public MenuCategory MenuCategory { get; set; }  // Navigation property
       
        public MenuItemType MenuItemType { get; set; }  // Navigation property

        public String Title { get; set; }

        public int TotalRatings { get; set; }

        public int PositiveRatings { get; set; }
    }
}