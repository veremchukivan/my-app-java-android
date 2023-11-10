using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using System.Net;
using WebAPI.Constants;
using WebAPI.Data.Entities.Identity;
using WebAPI.Helpers;
using WebAPI.Models.Account;

namespace WebAPI.Controllers
{
    [Route("api/accounts")]
    [ApiController]
    public class AccountController(UserManager<UserEntity> userManager,
                                   SignInManager<UserEntity> signInManager,
                                   JwtService jwtService) : ControllerBase
    {
        [HttpPost("register")]
        public async Task<IActionResult> Register([FromBody] RegisterUserViewModel model)
        {
            string imageName = null;

            if (!string.IsNullOrEmpty(model.ImageBase64))
            {
                imageName = ImageWorker.SaveImage(model.ImageBase64);
            }

            UserEntity user = new()
            {
                FirstName = model.FirstName,
                LastName = model.LastName,
                UserName = model.Email,
                Email = model.Email,
                Image = imageName,
            };

            var result = await userManager.CreateAsync(user, model.Password);
            if (result.Succeeded)
            {
                result = await userManager.AddToRoleAsync(user, Roles.User);
                return Ok();
            }
            else
            {
                return BadRequest();
            }
        }

        [HttpPost("login")]
        public async Task<LoginResponseViewModel> Login([FromBody] LoginViewModel model)
        {
            var user = await userManager.FindByEmailAsync(model.Email);

            await signInManager.SignInAsync(user, true);

            return new LoginResponseViewModel()
            {
                Token = jwtService.CreateToken(jwtService.GetClaims(user))
            };
        }
    }
}