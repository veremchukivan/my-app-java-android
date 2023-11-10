using System.IdentityModel.Tokens.Jwt;
using Microsoft.AspNetCore.Identity;
using System.Security.Claims;
using System.Text;
using Microsoft.IdentityModel.Tokens;
using WebAPI.Data.Entities.Identity;

namespace WebAPI.Helpers
{
    public class JwtService(IConfiguration configuration, UserManager<UserEntity> userManager)
    {
        public string CreateToken(IEnumerable<Claim> claims)
        {
            var jwtOpts = configuration.GetSection(nameof(JwtOptions)).Get<JwtOptions>();

            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(jwtOpts.Key));
            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);

            var token = new JwtSecurityToken(
                issuer: jwtOpts.Issuer,
                claims: claims,
                expires: DateTime.UtcNow.AddMinutes(jwtOpts.Lifetime),
                signingCredentials: credentials);

            return new JwtSecurityTokenHandler().WriteToken(token);
        }

        public IEnumerable<Claim> GetClaims(UserEntity user)
        {
            var claims = new List<Claim>
            {
                new (CustomClaimTypes.id, user.Id.ToString()),
                new (CustomClaimTypes.userName, user.FirstName),
                new (CustomClaimTypes.lastName, user.LastName),
                new (CustomClaimTypes.email, user.Email),
                new (CustomClaimTypes.image, user.Image),
            };

            var roles = userManager.GetRolesAsync(user).Result;
            claims.AddRange(roles.Select(role => new Claim(CustomClaimTypes.roles, role)));

            return claims;
        }
    }

    public static class CustomClaimTypes
    {
        public const string id = "id";
        public const string userName = "firstName";
        public const string lastName = "lastName";
        public const string email = "email";
        public const string roles = "roles";
        public const string image = "image";
    }
}
