using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace GauchoGrub.Models
{
    public class Menu
    {
        public int Id { get; set; }

        [Required]
        public int EventId { get; set; }  // Foreign Key

        [ForeignKey("EventId")]
        public RepeatedEvent Event { get; set; }  // Navigation property

        [Required]
        [DataType(DataType.Date)]
        public DateTime Date { get; set; }

        public ICollection<MenuItem> MenuItems { get; set; }  // Navigation property (Many-to-Many relationship)
    }
}