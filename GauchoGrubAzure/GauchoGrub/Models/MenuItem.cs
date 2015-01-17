using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace GauchoGrub.Models
{
    public class MenuItem
    {
        public int Id { get; set; }

        [Required]
        public int MenuCategoryId { get; set; } // Foreign key

        [Required]
        public int MenuItemTypeId { get; set; } // Foreign key

        [Required]
        public String Title { get; set; }
    }
}