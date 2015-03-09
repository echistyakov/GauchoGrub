using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace GauchoGrub.Models
{
    [DataContract]
    public class Rating
    {
        public int Id { get; set; }

        [Required]
        public int MenuId { get; set; }  // Foreign Key

        [Required]
        public int MenuItemId { get; set; }  // Foreign Key

        [Required]
        [DataMember]
        [DefaultValue(0)]
        public int TotalRatings { get; set; }  // TotalRatings for a specific MenuItem during a specific Menu

        [Required]
        [DataMember]
        [DefaultValue(0)]
        public int PositiveRatings { get; set; }  // PositiveRatings for a specific MenuItem during a specific Menu
    }
}