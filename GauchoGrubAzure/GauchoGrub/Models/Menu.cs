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
        public int EventId { get; set; }  // Foreign Key

        [Required]
        [DataType(DataType.Date)]
        public DateTime Date { get; set; }

        [Required]
        public List<int> MenuItems { get; set; }  // List of Foreign Keys
    }
}