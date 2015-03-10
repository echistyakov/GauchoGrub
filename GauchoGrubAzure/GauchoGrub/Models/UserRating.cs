using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace GauchoGrub.Models
{
    /*
     * UserRating - model for representing a unique user rating.
     * UserRatings are stored with respect to a specific Menu and specific MenuItem.
     * UserId is a unique identification string that helps prevent rating duplication.
     */
    [DataContract]
    public class UserRating
    {
        public int Id { get; set; }

        [Required]
        public int MenuId { get; set; }  // Foreign Key

        [Required]
        public int MenuItemId { get; set; }  // Foreign Key

        [Required]
        [MaxLength(32)]                     // This is usually a UUID
        public string UserId { get; set; }  // Unique User ID

        [Required]
        public bool PositiveRating { get; set; }  // True if rating is positive
    }
}