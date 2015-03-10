using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace GauchoGrub.Models
{
    /*
     * Rating - model for representing MenuItem ratings.
     * Ratings are stored with respect to a specific Menu and specific MenuItem.
     * This allows to track day-to-day, menu-to-menu variations in MenuItem rating.
     * Note: overall (not Menu specific) ratings are stored with the MenuItem model
     */
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