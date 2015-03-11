using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace GauchoGrub.Models
{
    /*
     * Menu - model for representing a menu.
     * Menu is related to a specific RepeatedEvent, DiningCommon,
     * has a specific Date and contains a list of MenuItems.
     * Has a many-to-many relationship with MenuItems.
     */
    [DataContract]
    public class Menu
    {
        [DataMember]
        public int Id { get; set; }

        [Required]
        public int EventId { get; set; }  // Foreign Key

        [DataMember]
        [ForeignKey("EventId")]
        public RepeatedEvent Event { get; set; }  // Navigation property

        [Required]
        [DataMember]
        [DataType(DataType.Date)]
        public DateTime Date { get; set; }

        [DataMember]
        public ICollection<MenuItem> MenuItems { get; set; }  // Navigation property (Many-to-Many relationship)
    }
}