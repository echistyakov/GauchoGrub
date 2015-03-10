using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace GauchoGrub.Models
{
    /*
     * MenuItem - model for representing food items.
     * Properties include: menu category, menu item type, rating count (total/positive).
     * Has a many-to-many relationship with Menus.
     */
    [DataContract]
    public class MenuItem
    {
        [DataMember]
        public int Id { get; set; }

        [Required]
        public int MenuCategoryId { get; set; } // Foreign key

        [DataMember]
        [ForeignKey("MenuCategoryId")]
        public MenuCategory MenuCategory { get; set; }  // Navigation property

        [Required]
        public int MenuItemTypeId { get; set; } // Foreign key

        [DataMember]
        [ForeignKey("MenuItemTypeId")]
        public MenuItemType MenuItemType { get; set; }  // Navigation property

        [Required]
        [DataMember]
        [MaxLength(450)]  // Unique constraint won't work without length limitation
        public String Title { get; set; }

        public ICollection<Menu> Menus { get; set; }  // Navigation property (Many-to-Many relationship)

        [DataMember]
        [DefaultValue(0)]
        public int TotalRatings { get; set; }

        [DataMember]
        [DefaultValue(0)]
        public int TotalPositiveRatings { get; set; }
    }
}