using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace GauchoGrub.Models
{
    // Breakfast, Brunch, Lunch, Dinner, Late Night, Sack Meal
    public class Meal
    {
        public int Id { get; set; }

        [Required]
        [Index(IsUnique = true)] // Extra duplication prevention
        [MaxLength(450)] // Unique constraint won't work without length limitation
        public String Name { get; set; }
    }
}