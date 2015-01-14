using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace GauchoGrub.Models
{
    // Regular, Vegetarian, Vegan
    public class MenuItemType
    {
        public int Id { get; set; }

        [Required]
        public String Name { get; set; }
    }
}