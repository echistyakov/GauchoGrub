using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace GauchoGrub.Models
{
    public class Menu
    {
        public int Id { get; set; }

        [Required]
        public int DiningCommonId { get; set; }  // Foreign Key

        [Required]
        public int EventId { get; set; }  // Foreign Key

        [Required]
        [DataType(DataType.Date)]
        public DateTime Date { get; set; }

        public List<MenuItem> MenuItems { get; set; }
    }
}