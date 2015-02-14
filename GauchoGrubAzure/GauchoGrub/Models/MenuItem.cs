using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace GauchoGrub.Models
{
    public class MenuItem
    {
        public int Id { get; set; }

        [Required]
        public int MenuCategoryId { get; set; } // Foreign key

        [ForeignKey("MenuCategoryId")]
        public MenuCategory MenuCategory { get; set; }  // Navigation property

        [Required]
        public int MenuItemTypeId { get; set; } // Foreign key

        [ForeignKey("MenuItemTypeId")]
        public MenuItemType MenuItemType { get; set; }  // Navigation property

        [Required]
        //[Index(IsUnique = true)]  // Extra duplication prevention
        [MaxLength(450)]  // Unique constraint won't work without length limitation
        public String Title { get; set; }

        public ICollection<Menu> Menus { get; set; }  // Navigation property (Many-to-Many relationship)
    }
}