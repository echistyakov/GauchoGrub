using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace GauchoGrub.Models
{
    // Breakfast, Brunch, Lunch, Dinner, Late Night, Sack Meal
    public class Meal
    {
        public int Id { get; set; }

        [Required]
        public String Name { get; set; }
    }
}