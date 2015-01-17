using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace GauchoGrub.Models
{
    public class RepeatedEvent
    {
        public int Id { get; set; }

        [Required]
        public int DiningCommonId { get; set; }  // Foreign Key

        [Required]
        public int MealId { get; set; }  // Foreign Key

        [Required]
        public TimeSpan From { get; set; }

        [Required]
        public TimeSpan To { get; set; }

        [Required]
        public DayOfWeek DayOfWeek { get; set; }
    }
}